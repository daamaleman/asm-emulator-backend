package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Alu;
import com.uam.asmemulator.cpu.Flags;
import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.parser.model.Operand;
import com.uam.asmemulator.parser.model.ParsedInstruction;

/** OR destino, fuente: O lógico bit a bit. Limpia CF y OF. */
public final class Or extends BinaryInstruction {

    public Or(ParsedInstruction parsed, Register destination, Operand source) {
        super(parsed, destination, source);
    }

    @Override
    protected long compute(long a, long b, int bits, Flags flags) {
        return Alu.or(a, b, bits, flags);
    }
}

