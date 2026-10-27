package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Cpu;
import com.uam.asmemulator.cpu.Flags;
import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.parser.model.ParsedInstruction;

/**
 * Instrucción de la forma "OP destino" → destino = OP destino.
 */
public abstract class UnaryInstruction extends AbstractInstruction {

    protected final Register destination;

    protected UnaryInstruction(ParsedInstruction parsed, Register destination) {
        super(parsed);
        this.destination = destination;
    }

    @Override
    public final void execute(Cpu cpu) {
        int bits = destination.bits();
        long value = cpu.registers().read(destination);
        cpu.registers().write(destination, compute(value, bits, cpu.flags()));
    }

    protected abstract long compute(long value, int bits, Flags flags);
}

