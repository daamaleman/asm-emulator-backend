package com.uam.asmemulator.api.dto;

import java.util.List;

public record TraceStepDto(int step, int line, String instruction, Boolean jumpTaken,
                           List<String> changedRegisters, CpuStateDto state) {
}

