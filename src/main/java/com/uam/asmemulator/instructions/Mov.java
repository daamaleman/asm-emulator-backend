package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Cpu;
import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.parser.model.Operand;
import com.uam.asmemulator.parser.model.ParsedInstruction;

/** MOV destino, fuente: copia el dato. No modifica banderas. */
public final class Mov extends AbstractInstruction {

    private final Register destination;
    private final Operand source;

    public Mov(ParsedInstruction parsed, Register destination, Operand source) {
        super(parsed);
        this.destination = destination;
        this.source = source;
    }

    @Override
    public void execute(Cpu cpu) {
        long value = Operands.read(source, cpu, destination.bits());
        cpu.registers().write(destination, value);
    }
}