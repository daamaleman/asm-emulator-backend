package com.uam.asmemulator.parser.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Programa analizado: instrucciones en orden y etiquetas apuntando
 * al índice de la instrucción que les sigue.
 */
public record Program(List<ParsedInstruction> instructions, Map<String, Integer> labels) {

    public Program {
        instructions = List.copyOf(instructions);
        labels = Collections.unmodifiableMap(new LinkedHashMap<>(labels));
    }

    public int size() {
        return instructions.size();
    }

    public int addressOf(String label) {
        Integer address = labels.get(label);
        if (address == null) {
            throw new IllegalArgumentException("Etiqueta no definida: " + label);
        }
        return address;
    }
}