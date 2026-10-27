package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Alu;
import com.uam.asmemulator.cpu.Flags;
import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.parser.model.Operand;
import com.uam.asmemulator.parser.model.ParsedInstruction;

/** CMP a, b: calcula a - b para actualizar banderas, sin modificar a. */
public final class Cmp extends CompareInstruction {

    public Cmp(ParsedInstruction parsed, Register left, Operand right) {
        super(parsed, left, right);
    }

    @Override
    protected void compare(long a, long b, int bits, Flags flags) {
        Alu.sub(a, b, bits, flags);
    }
}

