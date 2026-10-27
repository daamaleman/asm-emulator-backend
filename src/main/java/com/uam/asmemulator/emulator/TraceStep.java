package com.uam.asmemulator.emulator;

import com.uam.asmemulator.cpu.RegisterId;

import java.util.List;

/**
 * Una fila de la traza: la instrucción ejecutada y el estado después de ella.
 *
 * @param jumpTaken null si la instrucción no es un salto
 */
public record TraceStep(int step, int line, String instruction, Boolean jumpTaken,
                        List<RegisterId> changedRegisters, CpuSnapshot state) {

    public TraceStep {
        changedRegisters = List.copyOf(changedRegisters);
    }
}

