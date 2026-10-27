package com.uam.asmemulator.cpu;

/**
 * Estado de la CPU emulada: registros, banderas y puntero de instrucción.
 *
 * RIP se modela como el índice de la siguiente instrucción del programa
 * (no como una dirección en bytes), lo que basta para fines didácticos.
 */
public class Cpu {

    private final RegisterFile registers = new RegisterFile();
    private final Flags flags = new Flags();
    private int rip = 0;

    public RegisterFile registers() {
        return registers;
    }

    public Flags flags() {
        return flags;
    }

    public int rip() {
        return rip;
    }

    /** Salta a la instrucción con el índice dado (JMP, Jcc). */
    public void jumpTo(int index) {
        this.rip = index;
    }

    /** Avanza a la siguiente instrucción. */
    public void advance() {
        rip++;
    }

    public void reset() {
        registers.reset();
        flags.reset();
        rip = 0;
    }
}