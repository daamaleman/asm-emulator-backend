package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Cpu;
import com.uam.asmemulator.parser.model.Operand;
import com.uam.asmemulator.parser.model.ParsedInstruction;

/** PUSH fuente: RSP -= 8 y guarda la fuente en la pila. */
public final class Push extends AbstractInstruction {

    private final Operand source;

    public Push(ParsedInstruction parsed, Operand source) {
        super(parsed);
        this.source = source;
    }

    @Override
    public void execute(Cpu cpu) {
        cpu.push(Operands.read(source, cpu, 64));
    }
}

