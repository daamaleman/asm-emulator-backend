package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Cpu;
import com.uam.asmemulator.parser.model.ParsedInstruction;

/** JMP etiqueta: salto incondicional. */
public final class Jump extends AbstractInstruction implements JumpInstruction {

    private final int target;

    public Jump(ParsedInstruction parsed, int target) {
        super(parsed);
        this.target = target;
    }

    @Override
    public int target() {
        return target;
    }

    @Override
    public boolean willJump(Cpu cpu) {
        return true;
    }

    @Override
    public void execute(Cpu cpu) {
        cpu.jumpTo(target);
    }
}

