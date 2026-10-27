package com.uam.asmemulator.emulator;

import java.util.List;

public record ExecutionResult(CpuSnapshot initialState, CpuSnapshot finalState,
                              List<TraceStep> trace, int steps, StopReason stopReason) {

    public ExecutionResult {
        trace = List.copyOf(trace);
    }
}

