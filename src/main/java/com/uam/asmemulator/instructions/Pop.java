package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Cpu;
import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.parser.model.ParsedInstruction;

/** POP destino: saca el tope de la pila al registro y RSP += 8. */
public final class Pop extends AbstractInstruction {

    private final Register destination;

    public Pop(ParsedInstruction parsed, Register destination) {
        super(parsed);
        this.destination = destination;
    }

    @Override
    public void execute(Cpu cpu) {
        cpu.registers().write(destination, cpu.pop());
    }
}

