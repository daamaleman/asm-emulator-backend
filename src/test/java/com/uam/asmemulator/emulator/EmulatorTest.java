package com.uam.asmemulator.emulator;

import com.uam.asmemulator.cpu.Bits;
import com.uam.asmemulator.cpu.Cpu;
import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.cpu.RegisterId;
import com.uam.asmemulator.instructions.InstructionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class EmulatorTest {

    private static final String MAYOR = """
            mayor:
                mov eax, edi
                cmp edi, esi
                jg fin
                mov eax, esi
            fin:
                ret
            """;

    private static final String ACCESO = """
            acceso:
                cmp edi, 18
                jl menor
                mov eax, 1
                ret
            menor:
                xor eax, eax
                ret
            """;

    private static final String CONTAR = """
            contar:
                xor eax, eax
            inicio:
                cmp eax, 3
                jge fin
                add eax, 1
                jmp inicio
            fin:
                ret
            """;

    private static final String SUMAR_1_A_4 = """
                xor eax, eax   ; total=0
                mov ecx, 1     ; i=1
            inicio:
                cmp ecx, 4
                jg fin
                add eax, ecx
                add ecx, 1
                jmp inicio
            fin:
                ret
            """;

    private static final String ABSOLUTO = """
            absoluto:
                mov eax, edi
                cmp eax, 0
                jge fin
                neg eax
            fin:
                ret
            """;

    private final Emulator emulator = new Emulator();

    private ExecutionResult run(String code, long edi, long esi) {
        return emulator.run(code, Map.of(Register.EDI, edi, Register.ESI, esi));
    }

    private static long eax(ExecutionResult result) {
        return Bits.signExtend(result.finalState().get(RegisterId.RAX), 32);
    }

    private static long count(ExecutionResult result, String prefix) {
        return result.trace().stream().filter(s -> s.instruction().startsWith(prefix)).count();
    }

    @ParameterizedTest(name = "mayor({0},{1}) = {2}")
    @CsvSource({"8, 5, 8", "2, 9, 9", "5, 5, 5", "9, 4, 9", "2, 7, 7", "6, 6, 6"})
    void mayorReturnsGreaterValue(long a, long b, long expected) { // Diapositiva 7 y ejercicio 1
        ExecutionResult result = run(MAYOR, a, b);

        assertThat(eax(result)).isEqualTo(expected);
        assertThat(result.stopReason()).isEqualTo(StopReason.RETURN);
    }

    @Test
    void cmpDoesNotModifyOperands() { // "CMP no cambia EDI ni ESI"
        ExecutionResult result = run(MAYOR, 8, 5);

        assertThat(result.finalState().get(RegisterId.RDI)).isEqualTo(8L);
        assertThat(result.finalState().get(RegisterId.RSI)).isEqualTo(5L);
    }

    @Test
    void traceMarksTakenJump() {
        ExecutionResult result = run(MAYOR, 8, 5);
        TraceStep jg = result.trace().get(2);

        assertThat(jg.instruction()).isEqualTo("jg fin");
        assertThat(jg.jumpTaken()).isTrue();
        assertThat(result.trace().get(0).jumpTaken()).isNull();
        assertThat(result.steps()).isEqualTo(4);
    }

    @Test
    void traceReportsChangedRegisters() {
        ExecutionResult result = run(MAYOR, 8, 5);

        assertThat(result.trace().get(0).changedRegisters()).containsExactly(RegisterId.RAX);
        assertThat(result.trace().get(1).changedRegisters()).isEmpty(); // CMP solo cambia banderas
    }

    @ParameterizedTest(name = "acceso({0}) = {1}")
    @CsvSource({"17, 0", "18, 1", "30, 1", "-5, 0"})
    void accesoClassifiesAge(long edad, long expected) {
        assertThat(eax(run(ACCESO, edad, 0))).isEqualTo(expected);
    }

    @Test
    void contarMatchesTraceFromSlides() { // Diapositiva 11
        ExecutionResult result = emulator.run(CONTAR);

        assertThat(eax(result)).isEqualTo(3);
        assertThat(count(result, "add")).isEqualTo(3); // ADD se ejecuta exactamente 3 veces
        assertThat(count(result, "cmp")).isEqualTo(4); // la comparación ocurre 4 veces
        assertThat(result.steps()).isEqualTo(16);
    }

    @Test
    void sumar1a4ReturnsTen() { // Diapositiva 12
        assertThat(eax(emulator.run(SUMAR_1_A_4))).isEqualTo(10);
    }

    @Test
    void exercise3DoWhileLoop() { // Diapositiva 15: compara al final
        ExecutionResult result = emulator.run("""
                mov eax, 0
                mov ecx, 1
                inicio: add eax, ecx
                add ecx, 1
                cmp ecx, 5
                jl inicio
                ret
                """);

        assertThat(eax(result)).isEqualTo(10);
    }

    @ParameterizedTest(name = "absoluto({0}) = {1}")
    @CsvSource({"-3, 3", "0, 0", "5, 5"})
    void absolutoWithCmpJgeNeg(long x, long expected) { // Ejercicio 2
        assertThat(eax(run(ABSOLUTO, x, 0))).isEqualTo(expected);
    }

    @Test
    void jcJumpsOnCarry() {
        ExecutionResult result = emulator.run("""
                mov al, 0xFF
                add al, 1
                jc acarreo
                mov ebx, 1
                acarreo:
                mov ecx, 1
                """);

        assertThat(result.finalState().get(RegisterId.RBX)).isZero();
        assertThat(result.finalState().get(RegisterId.RCX)).isEqualTo(1L);
        assertThat(result.stopReason()).isEqualTo(StopReason.END_OF_PROGRAM);
    }

    @Test
    void testAndJzDetectZero() {
        ExecutionResult result = emulator.run("""
                mov eax, 0
                test eax, eax
                jz cero
                mov ebx, 1
                cero:
                mov ecx, 1
                """);

        assertThat(result.finalState().get(RegisterId.RBX)).isZero();
        assertThat(result.finalState().get(RegisterId.RCX)).isEqualTo(1L);
    }

    @Test
    void pushAndPopMoveValuesThroughStack() {
        ExecutionResult result = emulator.run("""
                mov rax, 5
                push rax
                mov rax, 0
                pop rbx
                """);

        assertThat(result.finalState().get(RegisterId.RBX)).isEqualTo(5L);
        assertThat(result.finalState().get(RegisterId.RSP)).isEqualTo(Cpu.INITIAL_STACK_POINTER);
        CpuSnapshot afterPush = result.trace().get(1).state();
        assertThat(afterPush.get(RegisterId.RSP)).isEqualTo(Cpu.INITIAL_STACK_POINTER - 8);
        assertThat(afterPush.stack()).containsValue(5L);
    }

    @Test
    void retStopsExecution() {
        ExecutionResult result = emulator.run("mov eax, 1\nret\nmov eax, 2");

        assertThat(eax(result)).isEqualTo(1);
        assertThat(result.steps()).isEqualTo(2);
        assertThat(result.stopReason()).isEqualTo(StopReason.RETURN);
    }

    @Test
    void infiniteLoopStopsAtStepLimit() {
        Emulator limited = new Emulator(new InstructionFactory(), 100);

        ExecutionResult result = limited.run("inicio:\n    jmp inicio");

        assertThat(result.stopReason()).isEqualTo(StopReason.STEP_LIMIT);
        assertThat(result.steps()).isEqualTo(100);
    }
}

