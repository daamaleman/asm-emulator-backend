package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Condition;
import com.uam.asmemulator.cpu.Cpu;
import com.uam.asmemulator.parser.model.ParsedInstruction;

import java.util.Map;

import static java.util.Map.entry;

/** Jcc etiqueta: salta solo si la condición de banderas se cumple. */
public final class ConditionalJump extends AbstractInstruction implements JumpInstruction {

    /** Mnemónicos y sus alias (JE = JZ, JC = JB, JNC = JAE, ...). */
    public static final Map<String, Condition> MNEMONICS = Map.ofEntries(
            entry("JE", Condition.EQUAL), entry("JZ", Condition.EQUAL),
            entry("JNE", Condition.NOT_EQUAL), entry("JNZ", Condition.NOT_EQUAL),
            // Con signo
            entry("JG", Condition.GREATER), entry("JNLE", Condition.GREATER),
            entry("JGE", Condition.GREATER_OR_EQUAL), entry("JNL", Condition.GREATER_OR_EQUAL),
            entry("JL", Condition.LESS), entry("JNGE", Condition.LESS),
            entry("JLE", Condition.LESS_OR_EQUAL), entry("JNG", Condition.LESS_OR_EQUAL),
            // Sin signo
            entry("JA", Condition.ABOVE), entry("JNBE", Condition.ABOVE),
            entry("JAE", Condition.ABOVE_OR_EQUAL), entry("JNB", Condition.ABOVE_OR_EQUAL),
            entry("JNC", Condition.ABOVE_OR_EQUAL),
            entry("JB", Condition.BELOW), entry("JNAE", Condition.BELOW), entry("JC", Condition.BELOW),
            entry("JBE", Condition.BELOW_OR_EQUAL), entry("JNA", Condition.BELOW_OR_EQUAL),
            // Signo y desbordamiento
            entry("JS", Condition.SIGN), entry("JNS", Condition.NOT_SIGN),
            entry("JO", Condition.OVERFLOW), entry("JNO", Condition.NOT_OVERFLOW));

    private final Condition condition;
    private final int target;

    public ConditionalJump(ParsedInstruction parsed, Condition condition, int target) {
        super(parsed);
        this.condition = condition;
        this.target = target;
    }

    public Condition condition() {
        return condition;
    }

    @Override
    public int target() {
        return target;
    }

    @Override
    public boolean willJump(Cpu cpu) {
        return condition.isMet(cpu.flags());
    }

    @Override
    public void execute(Cpu cpu) {
        if (willJump(cpu)) {
            cpu.jumpTo(target);
        }
    }
}

