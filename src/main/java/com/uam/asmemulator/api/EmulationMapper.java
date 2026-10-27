package com.uam.asmemulator.api;

import com.uam.asmemulator.api.dto.CpuStateDto;
import com.uam.asmemulator.api.dto.EmulateResponse;
import com.uam.asmemulator.api.dto.RegisterValueDto;
import com.uam.asmemulator.api.dto.StackEntryDto;
import com.uam.asmemulator.api.dto.TraceStepDto;
import com.uam.asmemulator.emulator.CpuSnapshot;
import com.uam.asmemulator.emulator.ExecutionResult;
import com.uam.asmemulator.emulator.TraceStep;

import java.util.List;

/**
 * Convierte el modelo del emulador a DTOs de la API.
 */
final class EmulationMapper {

    private EmulationMapper() {
    }

    static EmulateResponse toResponse(ExecutionResult result) {
        return new EmulateResponse(
                toDto(result.initialState()),
                toDto(result.finalState()),
                result.trace().stream().map(EmulationMapper::toDto).toList(),
                result.steps(),
                result.stopReason().name(),
                result.stopReason().description());
    }

    static CpuStateDto toDto(CpuSnapshot snapshot) {
        List<RegisterValueDto> registers = snapshot.registers().entrySet().stream()
                .map(e -> RegisterValueDto.of(e.getKey().name(), e.getValue()))
                .toList();
        List<StackEntryDto> stack = snapshot.stack().entrySet().stream()
                .map(e -> new StackEntryDto(RegisterValueDto.hex(e.getKey()), RegisterValueDto.hex(e.getValue())))
                .toList();
        return new CpuStateDto(registers, snapshot.flags(), snapshot.rip(), stack);
    }

    static TraceStepDto toDto(TraceStep step) {
        return new TraceStepDto(
                step.step(),
                step.line(),
                step.instruction(),
                step.jumpTaken(),
                step.changedRegisters().stream().map(Enum::name).toList(),
                toDto(step.state()));
    }
}

