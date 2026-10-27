package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Cpu;

/**
 * Instrucción que puede cambiar RIP.
 */
public interface JumpInstruction extends Instruction {

    /** Índice de la instrucción destino. */
    int target();

    /** Indica si el salto se tomará con el estado actual (antes de ejecutar). */
    boolean willJump(Cpu cpu);
}

