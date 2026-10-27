package com.uam.asmemulator.api.dto;

import java.util.Map;

/**
 * Ejemplo de clase listo para cargar en el emulador.
 *
 * @param cEquivalent función C equivalente, como en las diapositivas
 */
public record ExampleDto(String id, String title, String session, String cEquivalent,
                         String code, Map<String, String> initialRegisters) {
}

