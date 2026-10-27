package com.uam.asmemulator.emulator;

import com.uam.asmemulator.cpu.Cpu;
import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.instructions.Instruction;
import com.uam.asmemulator.instructions.InstructionFactory;
import com.uam.asmemulator.instructions.JumpInstruction;
import com.uam.asmemulator.parser.Parser;
import com.uam.asmemulator.parser.model.Program;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Ejecuta un programa completo: ciclo búsqueda → decodificación → ejecución,
 * guardando el estado de la CPU después de cada instrucción.
 */
public class Emulator {

    public static final int DEFAULT_MAX_STEPS = 10_000;

    private final InstructionFactory factory;
    private final int maxSteps;

    public Emulator() {
        this(new InstructionFactory(), DEFAULT_MAX_STEPS);
    }

    public Emulator(InstructionFactory factory, int maxSteps) {
        this.factory = factory;
        this.maxSteps = maxSteps;
    }

    public ExecutionResult run(String code) {
        return run(code, Map.of());
    }

    /**
     * @param initialValues valores iniciales de registros (p. ej. EDI=8, ESI=5)
     */
    public ExecutionResult run(String code, Map<Register, Long> initialValues) {
        Program program = Parser.parse(code);
        List<Instruction> instructions = factory.createAll(program);

        Cpu cpu = new Cpu();
        initialValues.forEach(cpu.registers()::write);

        CpuSnapshot initialState = CpuSnapshot.of(cpu);
        CpuSnapshot previous = initialState;
        List<TraceStep> trace = new ArrayList<>();
        StopReason stopReason = StopReason.END_OF_PROGRAM;
        int steps = 0;

        while (cpu.rip() < instructions.size()) {
            if (steps >= maxSteps) {
                stopReason = StopReason.STEP_LIMIT;
                break;
            }

            // Búsqueda
            Instruction instruction = instructions.get(cpu.rip());
            Boolean jumpTaken = instruction instanceof JumpInstruction jump ? jump.willJump(cpu) : null;

            // RIP apunta a la siguiente instrucción antes de ejecutar (como en x86)
            cpu.advance();
            instruction.execute(cpu);
            steps++;

            CpuSnapshot current = CpuSnapshot.of(cpu);
            trace.add(new TraceStep(
                    steps,
                    instruction.parsed().line(),
                    instruction.parsed().sourceText(),
                    jumpTaken,
                    current.changedSince(previous),
                    current));
            previous = current;

            if (cpu.halted()) {
                stopReason = StopReason.RETURN;
                break;
            }
        }

        return new ExecutionResult(initialState, previous, trace, steps, stopReason);
    }
}

