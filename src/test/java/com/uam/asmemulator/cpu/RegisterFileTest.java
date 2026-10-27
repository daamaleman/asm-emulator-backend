package com.uam.asmemulator.cpu;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterFileTest {

    private final RegisterFile registers = new RegisterFile();

    @Test
    void writing32BitViewClearsUpperHalf() {
        registers.write(Register.RAX, 0xFFFF_FFFF_FFFF_FFFFL);
        registers.write(Register.EAX, 5);

        assertThat(registers.read(Register.RAX)).isEqualTo(5L);
    }

    @Test
    void writing16BitViewPreservesOtherBits() {
        registers.write(Register.RAX, 0x1122_3344_5566_7788L);
        registers.write(Register.AX, 0xAAAA);

        assertThat(registers.read(Register.RAX)).isEqualTo(0x1122_3344_5566_AAAAL);
    }

    @Test
    void writing8BitViewPreservesOtherBits() {
        registers.write(Register.RAX, 0x1122_3344_5566_7788L);
        registers.write(Register.AL, 0xFF);

        assertThat(registers.read(Register.RAX)).isEqualTo(0x1122_3344_5566_77FFL);
    }

    @Test
    void readsNarrowViewsOfSameRegister() {
        registers.write(Register.RAX, 0x1122_3344_5566_7788L);

        assertThat(registers.read(Register.EAX)).isEqualTo(0x5566_7788L);
        assertThat(registers.read(Register.AX)).isEqualTo(0x7788L);
        assertThat(registers.read(Register.AL)).isEqualTo(0x88L);
    }

    @Test
    void readSignedInterpretsTwosComplement() {
        registers.write(Register.EAX, -1);

        assertThat(registers.read(Register.EAX)).isEqualTo(0xFFFF_FFFFL);
        assertThat(registers.readSigned(Register.EAX)).isEqualTo(-1L);
        assertThat(registers.read(Register.RAX)).isEqualTo(0xFFFF_FFFFL);
    }

    @Test
    void extendedRegistersAreIndependent() {
        registers.write(Register.R8D, 7);

        assertThat(registers.read(Register.R8)).isEqualTo(7L);
        assertThat(registers.read(Register.R9)).isZero();
    }
}