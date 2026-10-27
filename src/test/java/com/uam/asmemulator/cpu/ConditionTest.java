package com.uam.asmemulator.cpu;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class ConditionTest {

    /** Simula CMP a, b con operandos de 32 bits. */
    private static Flags compare(long a, long b) {
        Flags flags = new Flags();
        Alu.sub(a, b, 32, flags);
        return flags;
    }

    @ParameterizedTest(name = "mayor({0},{1}) → JG tomado: {2}")
    @CsvSource({"8, 5, true", "2, 9, false", "5, 5, false", "9, 4, true", "6, 6, false"})
    void jgFollowsMayorExample(long a, long b, boolean taken) {
        assertThat(Condition.GREATER.isMet(compare(a, b))).isEqualTo(taken);
    }

    @ParameterizedTest(name = "acceso({0}) → JL tomado: {1}")
    @CsvSource({"17, true", "18, false", "30, false", "-5, true"})
    void jlFollowsAccesoExample(long edad, boolean taken) {
        assertThat(Condition.LESS.isMet(compare(edad, 18))).isEqualTo(taken);
    }

    @ParameterizedTest(name = "contar: EAX={0} → JGE tomado: {1}")
    @CsvSource({"0, false", "1, false", "2, false", "3, true"})
    void jgeFollowsWhileTrace(long eax, boolean taken) {
        assertThat(Condition.GREATER_OR_EQUAL.isMet(compare(eax, 3))).isEqualTo(taken);
    }

    @Test
    void jaIsWrongForNegativeSignedValues() { // Ejercicio 4: a=-1, b=1
        Flags flags = compare(-1, 1);

        assertThat(Condition.ABOVE.isMet(flags)).isTrue();    // sin signo: 0xFFFFFFFF > 1
        assertThat(Condition.GREATER.isMet(flags)).isFalse(); // con signo: -1 < 1
        assertThat(Condition.LESS.isMet(flags)).isTrue();
    }

    @Test
    void equalityConditionsUseZeroFlag() {
        assertThat(Condition.EQUAL.isMet(compare(5, 5))).isTrue();
        assertThat(Condition.NOT_EQUAL.isMet(compare(5, 5))).isFalse();
    }

    @Test
    void carryConditionMatchesUnsignedBorrow() { // JC / JB
        assertThat(Condition.BELOW.isMet(compare(2, 9))).isTrue();
        assertThat(Condition.BELOW.isMet(compare(9, 2))).isFalse();
    }
}