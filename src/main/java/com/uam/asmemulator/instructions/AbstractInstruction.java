package com.uam.asmemulator.instructions;

import com.uam.asmemulator.parser.model.ParsedInstruction;

public abstract class AbstractInstruction implements Instruction {

    private final ParsedInstruction parsed;

    protected AbstractInstruction(ParsedInstruction parsed) {
        this.parsed = parsed;
    }

    @Override
    public ParsedInstruction parsed() {
        return parsed;
    }

    @Override
    public String toString() {
        return parsed.sourceText();
    }
}