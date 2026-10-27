package com.uam.asmemulator.api.dto;

import com.uam.asmemulator.emulator.FlagsSnapshot;

import java.util.List;

public record CpuStateDto(List<RegisterValueDto> registers, FlagsSnapshot flags, int rip,
                          List<StackEntryDto> stack) {
}

