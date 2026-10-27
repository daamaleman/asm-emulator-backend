package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Cpu;
import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.parser.Parser;
import org.junit.jupiter.api.Test;

import static com.uam.asmemulator.cpu.Register.*;
import static org.assertj.core.api.Assertions.assertThat;

class InstructionExecutionTest {

    private final Cpu cpu = new Cpu();
    private final InstructionFactory factory = new InstructionFactory();

    /** Ejecuta código lineal (sin saltos) sobre la CPU de la prueba. */
    private void run(String code) {
        for (Instruction instruction : factory.createAll(Parser.parse(code))) {
            cpu.advance();
            instruction.execute(cpu);
        }
    }

    private void set(Register register, long value) {
        cpu.registers().write(register, value);
    }

    private long signed(Register register) {
        return cpu.registers().readSigned(register);
    }

    @Test
    void sumarExample() { // Sesión 1, diapositiva 8
        set(EDI, 3);
        set(ESI, 5);
        run("""
                mov eax, edi
                add eax, esi
                """);
        assertThat(signed(EAX)).isEqualTo(8);
    }

    @Test
    void operarExample() { // (a - b) * 2, diapositiva 9
        set(EDI, 7);
        set(ESI, 4);
        run("""
                mov eax, edi
                sub eax, esi
                add eax, eax
                """);
        assertThat(signed(EAX)).isEqualTo(6);
    }

    @Test
    void misterioExample() { // a - b + 5 con a=8, b=3, diapositiva 10
        set(EDI, 8);
        set(ESI, 3);
        run("""
                mov eax, edi
                sub eax, esi
                add eax, 5
                """);
        assertThat(signed(EAX)).isEqualTo(10);
    }

    @Test
    void exercise3WithImul() { // a * b - 1 con a=4, b=3, diapositiva 15
        set(EDI, 4);
        set(ESI, 3);
        run("""
                mov eax, edi
                imul eax, esi
                sub eax, 1
                """);
        assertThat(signed(EAX)).isEqualTo(11);
    }

    @Test
    void dobleSumaExercise() { // (a + b) * 2 con a=3, b=5, diapositiva 14
        set(EDI, 3);
        set(ESI, 5);
        run("""
                mov eax, edi
                add eax, esi
                add eax, eax
                """);
        assertThat(signed(EAX)).isEqualTo(16);
    }

    @Test
    void negTurnsNegativeIntoPositive() { // absoluto(-3)
        set(EDI, -3);
        run("""
                mov eax, edi
                neg eax
                """);
        assertThat(signed(EAX)).isEqualTo(3);
    }

    @Test
    void xorZeroesRegisterAndSetsZeroFlag() {
        set(EAX, 42);
        run("xor eax, eax");
        assertThat(signed(EAX)).isZero();
        assertThat(cpu.flags().zf()).isTrue();
    }

    @Test
    void writing32BitDestinationClearsUpperHalf() {
        run("""
                mov rax, -1
                mov eax, 5
                """);
        assertThat(cpu.registers().read(RAX)).isEqualTo(5L);
    }

    @Test
    void eightBitAddWrapsAndPreservesUpperBits() {
        run("""
                mov eax, 0x1200
                mov al, 0xFF
                add al, 1
                """);
        assertThat(cpu.registers().read(EAX)).isEqualTo(0x1200L);
        assertThat(cpu.flags().cf()).isTrue();
        assertThat(cpu.flags().zf()).isTrue();
    }

    @Test
    void incAndDec() {
        run("""
                mov ecx, 1
                inc ecx
                inc ecx
                dec ecx
                """);
        assertThat(signed(ECX)).isEqualTo(2);
    }

    @Test
    void notInvertsBitsWithoutChangingFlags() {
        run("""
                mov eax, 0
                xor ebx, ebx
                not eax
                """);
        assertThat(cpu.registers().read(EAX)).isEqualTo(0xFFFF_FFFFL);
        assertThat(cpu.flags().zf()).isTrue(); // sigue igual desde el XOR
    }

    @Test
    void andAndOrWorkBitwise() {
        run("""
                mov eax, 0b1100
                and eax, 0b1010
                mov ebx, 0b1100
                or ebx, 0b0011
                """);
        assertThat(signed(EAX)).isEqualTo(0b1000);
        assertThat(signed(EBX)).isEqualTo(0b1111);
    }

    @Test
    void imulSetsOverflowWhenProductDoesNotFit() {
        run("""
                mov eax, 0x10000
                imul eax, eax
                """);
        assertThat(signed(EAX)).isZero();
        assertThat(cpu.flags().of()).isTrue();
    }
}

