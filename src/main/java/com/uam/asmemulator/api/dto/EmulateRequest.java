package com.uam.asmemulator.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Map;

/**
 * @param code             código ensamblador
 * @param initialRegisters valores iniciales, p. ej. {"EDI": "8", "ESI": "0x5"}
 */
public record EmulateRequest(
        @NotBlank(message = "El código no puede estar vacío")
        @Size(max = 20_000, message = "El código no puede superar 20000 caracteres")
        String code,
        Map<String, String> initialRegisters) {
}

