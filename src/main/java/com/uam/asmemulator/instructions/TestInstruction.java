package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Alu;
import com.uam.asmemulator.cpu.Flags;
import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.parser.model.Operand;
import com.uam.asmemulator.parser.model.ParsedInstruction;

/** TEST a, b: calcula a AND b para actualizar banderas, sin modificar a. */
public final class TestInstruction extends CompareInstruction {

    public TestInstruction(ParsedInstruction parsed, Register left, Operand right) {
        super(parsed, left, right);
    }

    @Override
    protected void compare(long a, long b, int bits, Flags flags) {
        Alu.and(a, b, bits, flags);
    }
}

