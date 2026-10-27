package com.uam.asmemulator.emulator;

import com.uam.asmemulator.cpu.Cpu;
import com.uam.asmemulator.cpu.RegisterId;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Copia inmutable del estado de la CPU en un instante.
 */
public record CpuSnapshot(Map<RegisterId, Long> registers, FlagsSnapshot flags, int rip,
                          Map<Long, Long> stack) {

    public CpuSnapshot {
        registers = Collections.unmodifiableMap(new EnumMap<>(registers));
        stack = Collections.unmodifiableMap(new TreeMap<>(stack));
    }

    public static CpuSnapshot of(Cpu cpu) {
        Map<RegisterId, Long> values = new EnumMap<>(RegisterId.class);
        for (RegisterId id : RegisterId.values()) {
            values.put(id, cpu.registers().get(id));
        }
        return new CpuSnapshot(values, FlagsSnapshot.of(cpu.flags()), cpu.rip(), cpu.stack().contents());
    }

    public long get(RegisterId id) {
        return registers.get(id);
    }

    /** Registros cuyo valor cambió respecto al estado anterior. */
    public List<RegisterId> changedSince(CpuSnapshot previous) {
        return Arrays.stream(RegisterId.values())
                .filter(id -> !registers.get(id).equals(previous.registers.get(id)))
                .toList();
    }
}

