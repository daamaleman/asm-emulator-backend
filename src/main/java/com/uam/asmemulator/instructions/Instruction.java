package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Cpu;
import com.uam.asmemulator.parser.model.ParsedInstruction;

/**
 * Instrucción ejecutable (patrón Command).
 *
 * Como en un CPU real, cuando se ejecuta, RIP ya apunta a la siguiente
 * instrucción; los saltos lo sobrescriben.
 */
public interface Instruction {

    void execute(Cpu cpu);

    /** Instrucción original del parser (línea y texto fuente). */
    ParsedInstruction parsed();
}