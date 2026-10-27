package com.uam.asmemulator.cpu;

import java.util.Arrays;

/**
 * Valores de los 16 registros físicos de 64 bits.
 */
public class RegisterFile {

    private final long[] values = new long[RegisterId.values().length];

    /** Lee la vista sin signo (EAX devuelve solo los 32 bits bajos de RAX). */
    public long read(Register register) {
        return values[register.id().ordinal()] & register.mask();
    }

    /** Lee la vista interpretada con signo. */
    public long readSigned(Register register) {
        return Bits.signExtend(read(register), register.bits());
    }

    public void write(Register register, long value) {
        int index = register.id().ordinal();
        long masked = value & register.mask();
        switch (register.bits()) {
            // En x86-64, escribir 32 bits pone en cero la parte alta del registro
            case 64, 32 -> values[index] = masked;
            // 16 y 8 bits conservan el resto del registro
            default -> values[index] = (values[index] & ~register.mask()) | masked;
        }
    }

    public long get(RegisterId id) {
        return values[id.ordinal()];
    }

    public void set(RegisterId id, long value) {
        values[id.ordinal()] = value;
    }

    public void reset() {
        Arrays.fill(values, 0L);
    }
}