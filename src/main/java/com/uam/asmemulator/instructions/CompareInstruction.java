package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Cpu;
import com.uam.asmemulator.cpu.Flags;
import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.parser.model.Operand;
import com.uam.asmemulator.parser.model.ParsedInstruction;

/**
 * Calcula una operación solo para actualizar banderas; no guarda el resultado.
 */
public abstract class CompareInstruction extends AbstractInstruction {

    protected final Register left;
    protected final Operand right;

    protected CompareInstruction(ParsedInstruction parsed, Register left, Operand right) {
        super(parsed);
        this.left = left;
        this.right = right;
    }

    @Override
    public final void execute(Cpu cpu) {
        int bits = left.bits();
        long a = cpu.registers().read(left);
        long b = Operands.read(right, cpu, bits);
        compare(a, b, bits, cpu.flags());
    }

    protected abstract void compare(long a, long b, int bits, Flags flags);
}

