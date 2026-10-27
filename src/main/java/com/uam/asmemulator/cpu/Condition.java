package com.uam.asmemulator.cpu;

import java.util.function.Predicate;

/**
 * Condiciones de los saltos Jcc, evaluadas a partir de las banderas.
 */
public enum Condition {
    EQUAL(f -> f.zf(), "ZF"),
    NOT_EQUAL(f -> !f.zf(), "!ZF"),
    GREATER(f -> !f.zf() && f.sf() == f.of(), "!ZF && SF == OF"),
    GREATER_OR_EQUAL(f -> f.sf() == f.of(), "SF == OF"),
    LESS(f -> f.sf() != f.of(), "SF != OF"),
    LESS_OR_EQUAL(f -> f.zf() || f.sf() != f.of(), "ZF || SF != OF"),
    ABOVE(f -> !f.cf() && !f.zf(), "!CF && !ZF"),
    ABOVE_OR_EQUAL(f -> !f.cf(), "!CF"),
    BELOW(f -> f.cf(), "CF"),
    BELOW_OR_EQUAL(f -> f.cf() || f.zf(), "CF || ZF"),
    SIGN(f -> f.sf(), "SF"),
    NOT_SIGN(f -> !f.sf(), "!SF"),
    OVERFLOW(f -> f.of(), "OF"),
    NOT_OVERFLOW(f -> !f.of(), "!OF");

    private final Predicate<Flags> test;
    private final String flagsExpression;

    Condition(Predicate<Flags> test, String flagsExpression) {
        this.test = test;
        this.flagsExpression = flagsExpression;
    }

    public boolean isMet(Flags flags) {
        return test.test(flags);
    }

    /** Fórmula en términos de banderas, p. ej. "!ZF && SF == OF" para JG. */
    public String flagsExpression() {
        return flagsExpression;
    }
}