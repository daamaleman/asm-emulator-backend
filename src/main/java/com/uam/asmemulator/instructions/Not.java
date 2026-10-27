package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Alu;
import com.uam.asmemulator.cpu.Flags;
import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.parser.model.ParsedInstruction;

/** NOT destino: invierte todos los bits. No modifica banderas. */
public final class Not extends UnaryInstruction {

    public Not(ParsedInstruction parsed, Register destination) {
        super(parsed, destination);
    }

    @Override
    protected long compute(long value, int bits, Flags flags) {
        return Alu.not(value, bits);
    }
}

