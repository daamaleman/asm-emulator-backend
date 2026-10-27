package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Cpu;
import com.uam.asmemulator.cpu.Flags;
import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.parser.model.Operand;
import com.uam.asmemulator.parser.model.ParsedInstruction;

/**
 * Instrucción de la forma "OP destino, fuente" → destino = destino OP fuente.
 */
public abstract class BinaryInstruction extends AbstractInstruction {

    protected final Register destination;
    protected final Operand source;

    protected BinaryInstruction(ParsedInstruction parsed, Register destination, Operand source) {
        super(parsed);
        this.destination = destination;
        this.source = source;
    }

    @Override
    public final void execute(Cpu cpu) {
        int bits = destination.bits();
        long a = cpu.registers().read(destination);
        long b = Operands.read(source, cpu, bits);
        long result = compute(a, b, bits, cpu.flags());
        cpu.registers().write(destination, result);
    }

    protected abstract long compute(long a, long b, int bits, Flags flags);
}