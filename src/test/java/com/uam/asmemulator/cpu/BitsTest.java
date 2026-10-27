package com.uam.asmemulator.cpu;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BitsTest {

    @Test
    void truncatesToWidth() {
        assertThat(Bits.truncate(-1L, 16)).isEqualTo(0xFFFFL);
        assertThat(Bits.truncate(0x1_0000_0005L, 32)).isEqualTo(5L);
    }

    @Test
    void signExtendsNegativeAndPositiveValues() {
        assertThat(Bits.signExtend(0xFFL, 8)).isEqualTo(-1L);
        assertThat(Bits.signExtend(0x7FL, 8)).isEqualTo(127L);
        assertThat(Bits.signExtend(0xFFFF_FFF9L, 32)).isEqualTo(-7L);
    }

    @Test
    void detectsSignBit() {
        assertThat(Bits.signBit(0x8000_0000L, 32)).isTrue();
        assertThat(Bits.signBit(0x7FFF_FFFFL, 32)).isFalse();
    }

    @Test
    void checksSignedAndUnsignedRanges() {
        assertThat(Bits.fits(255, 8)).isTrue();
        assertThat(Bits.fits(-128, 8)).isTrue();
        assertThat(Bits.fits(256, 8)).isFalse();
        assertThat(Bits.fits(-129, 8)).isFalse();
        assertThat(Bits.fits(Long.MIN_VALUE, 64)).isTrue();
    }
}