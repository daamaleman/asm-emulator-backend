package com.uam.asmemulator.api.dto;

import java.util.List;

public record EmulateResponse(CpuStateDto initialState, CpuStateDto finalState,
                              List<TraceStepDto> trace, int steps,
                              String stopReason, String stopMessage) {
}

