package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Alu;
import com.uam.asmemulator.cpu.Flags;
import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.parser.model.Operand;
import com.uam.asmemulator.parser.model.ParsedInstruction;

/** ADD destino, fuente: suma y actualiza banderas. */
public final class Add extends BinaryInstruction {

    public Add(ParsedInstruction parsed, Register destination, Operand source) {
        super(parsed, destination, source);
    }

    @Override
    protected long compute(long a, long b, int bits, Flags flags) {
        return Alu.add(a, b, bits, flags);
    }
}