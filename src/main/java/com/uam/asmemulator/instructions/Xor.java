package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Alu;
import com.uam.asmemulator.cpu.Flags;
import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.parser.model.Operand;
import com.uam.asmemulator.parser.model.ParsedInstruction;

/** XOR destino, fuente: O exclusivo. "xor eax, eax" pone EAX en cero. */
public final class Xor extends BinaryInstruction {

    public Xor(ParsedInstruction parsed, Register destination, Operand source) {
        super(parsed, destination, source);
    }

    @Override
    protected long compute(long a, long b, int bits, Flags flags) {
        return Alu.xor(a, b, bits, flags);
    }
}

