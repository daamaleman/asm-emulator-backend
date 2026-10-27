package com.uam.asmemulator.api.dto;

/**
 * Valor de un registro de 64 bits en tres representaciones (como texto).
 */
public record RegisterValueDto(String name, String hex, String unsigned, String signed) {

    public static RegisterValueDto of(String name, long value) {
        return new RegisterValueDto(name, hex(value), Long.toUnsignedString(value), Long.toString(value));
    }

    public static String hex(long value) {
        return "0x%016X".formatted(value);
    }
}

