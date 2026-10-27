package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Alu;
import com.uam.asmemulator.cpu.Flags;
import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.parser.model.ParsedInstruction;

/** DEC destino: resta 1 sin modificar CF. */
public final class Dec extends UnaryInstruction {

    public Dec(ParsedInstruction parsed, Register destination) {
        super(parsed, destination);
    }

    @Override
    protected long compute(long value, int bits, Flags flags) {
        return Alu.dec(value, bits, flags);
    }
}

