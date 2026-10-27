package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Alu;
import com.uam.asmemulator.cpu.Flags;
import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.parser.model.Operand;
import com.uam.asmemulator.parser.model.ParsedInstruction;

/** SUB destino, fuente: resta y actualiza banderas. */
public final class Sub extends BinaryInstruction {

    public Sub(ParsedInstruction parsed, Register destination, Operand source) {
        super(parsed, destination, source);
    }

    @Override
    protected long compute(long a, long b, int bits, Flags flags) {
        return Alu.sub(a, b, bits, flags);
    }
}

