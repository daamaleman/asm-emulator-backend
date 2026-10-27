package com.uam.asmemulator.cpu;

/**
 * Estado de la CPU emulada: registros, banderas, puntero de instrucción y pila.
 *
 * RIP se modela como el índice de la siguiente instrucción del programa
 * (no como una dirección en bytes), lo que basta para fines didácticos.
 */
public class Cpu {

    /** Valor inicial de RSP; la pila crece hacia direcciones menores. */
    public static final long INITIAL_STACK_POINTER = 0x7FFF_FFF0L;

    private final RegisterFile registers = new RegisterFile();
    private final Flags flags = new Flags();
    private final StackMemory stack = new StackMemory();
    private int rip;
    private boolean halted;

    public Cpu() {
        reset();
    }

    public RegisterFile registers() {
        return registers;
    }

    public Flags flags() {
        return flags;
    }

    public StackMemory stack() {
        return stack;
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

    /** PUSH: RSP -= 8 y guarda el valor en [RSP]. */
    public void push(long value) {
        long rsp = registers.get(RegisterId.RSP) - 8;
        registers.set(RegisterId.RSP, rsp);
        stack.write(rsp, value);
    }

    /** POP: lee [RSP] y luego RSP += 8. */
    public long pop() {
        long rsp = registers.get(RegisterId.RSP);
        long value = stack.read(rsp);
        registers.set(RegisterId.RSP, rsp + 8);
        return value;
    }

    public void halt() {
        halted = true;
    }

    public boolean halted() {
        return halted;
    }

    public void reset() {
        registers.reset();
        flags.reset();
        stack.reset();
        registers.set(RegisterId.RSP, INITIAL_STACK_POINTER);
        rip = 0;
        halted = false;
    }
}