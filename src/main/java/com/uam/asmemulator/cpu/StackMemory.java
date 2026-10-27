package com.uam.asmemulator.cpu;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Memoria de pila simplificada: palabras de 64 bits indexadas por dirección.
 * Las direcciones nunca escritas valen 0.
 */
public class StackMemory {

    private final Map<Long, Long> words = new TreeMap<>();

    public long read(long address) {
        return words.getOrDefault(address, 0L);
    }

    public void write(long address, long value) {
        words.put(address, value);
    }

    public Map<Long, Long> contents() {
        return Collections.unmodifiableMap(words);
    }

    public void reset() {
        words.clear();
    }
}

