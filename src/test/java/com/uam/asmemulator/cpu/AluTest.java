package com.uam.asmemulator.cpu;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AluTest {

    private final Flags flags = new Flags();

    @Test
    void compareEqualValuesSetsZeroFlag() { // Diapositiva: CMP 5,5 → ZF=1
        Alu.sub(5, 5, 32, flags);

        assertThat(flags.zf()).isTrue();
        assertThat(flags.sf()).isFalse();
        assertThat(flags.cf()).isFalse();
        assertThat(flags.of()).isFalse();
    }

    @Test
    void subWithNegativeResultSetsSignAndCarry() { // 2 - 9
        long result = Alu.sub(2, 9, 32, flags);

        assertThat(Bits.signExtend(result, 32)).isEqualTo(-7L);
        assertThat(flags.sf()).isTrue();
        assertThat(flags.cf()).isTrue();
        assertThat(flags.of()).isFalse();
        assertThat(flags.zf()).isFalse();
    }

    @Test
    void addSignedOverflowSetsOverflowFlag() {
        long result = Alu.add(0x7FFF_FFFFL, 1, 32, flags);

        assertThat(result).isEqualTo(0x8000_0000L);
        assertThat(flags.of()).isTrue();
        assertThat(flags.sf()).isTrue();
        assertThat(flags.cf()).isFalse();
    }

    @Test
    void addUnsignedOverflowSetsCarryFlag() {
        long result = Alu.add(0xFFFF_FFFFL, 1, 32, flags);

        assertThat(result).isZero();
        assertThat(flags.cf()).isTrue();
        assertThat(flags.zf()).isTrue();
        assertThat(flags.of()).isFalse();
    }

    @Test
    void add64BitCarry() {
        Alu.add(-1L, 1, 64, flags);

        assertThat(flags.cf()).isTrue();
        assertThat(flags.zf()).isTrue();
    }

    @Test
    void logicOperationsClearCarryAndOverflow() { // XOR EAX,EAX
        flags.setCf(true);
        flags.setOf(true);

        long result = Alu.xor(7, 7, 32, flags);

        assertThat(result).isZero();
        assertThat(flags.zf()).isTrue();
        assertThat(flags.cf()).isFalse();
        assertThat(flags.of()).isFalse();
    }

    @Test
    void incPreservesCarryFlag() {
        flags.setCf(true);

        assertThat(Alu.inc(1, 32, flags)).isEqualTo(2L);
        assertThat(flags.cf()).isTrue();
    }

    @Test
    void negOfIntMinOverflows() { // Ejercicio 2: el caso INT_MIN
        long result = Alu.neg(0x8000_0000L, 32, flags);

        assertThat(result).isEqualTo(0x8000_0000L);
        assertThat(flags.of()).isTrue();
        assertThat(flags.cf()).isTrue();
    }

    @Test
    void negOfZeroClearsCarry() {
        assertThat(Alu.neg(0, 32, flags)).isZero();
        assertThat(flags.cf()).isFalse();
        assertThat(flags.zf()).isTrue();
    }

    @Test
    void imulWithoutOverflow() { // Ejercicio 3: 4 * 3
        assertThat(Alu.imul(4, 3, 32, flags)).isEqualTo(12L);
        assertThat(flags.cf()).isFalse();
        assertThat(flags.of()).isFalse();
    }

    @Test
    void imulWithNegativeOperand() {
        long result = Alu.imul(-4, 3, 32, flags);

        assertThat(Bits.signExtend(result, 32)).isEqualTo(-12L);
        assertThat(flags.of()).isFalse();
    }

    @Test
    void imulOverflowIn32Bits() {
        assertThat(Alu.imul(0x10000, 0x10000, 32, flags)).isZero();
        assertThat(flags.cf()).isTrue();
        assertThat(flags.of()).isTrue();
    }

    @Test
    void imulOverflowIn64Bits() {
        Alu.imul(Long.MAX_VALUE, 2, 64, flags);

        assertThat(flags.of()).isTrue();
    }
}