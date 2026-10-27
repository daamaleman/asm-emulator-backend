package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Alu;
import com.uam.asmemulator.cpu.Flags;
import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.parser.model.ParsedInstruction;

/** NEG destino: cambia el signo (0 - destino). */
public final class Neg extends UnaryInstruction {

    public Neg(ParsedInstruction parsed, Register destination) {
        super(parsed, destination);
    }

    @Override
    protected long compute(long value, int bits, Flags flags) {
        return Alu.neg(value, bits, flags);
    }
}

