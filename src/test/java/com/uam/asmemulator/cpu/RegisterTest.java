package com.uam.asmemulator.cpu;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterTest {

    @Test
    void findsRegisterIgnoringCase() {
        assertThat(Register.fromName("eax")).contains(Register.EAX);
        assertThat(Register.fromName("R8d")).contains(Register.R8D);
    }

    @Test
    void returnsEmptyForUnknownName() {
        assertThat(Register.fromName("inicio")).isEmpty();
    }

    @Test
    void viewsShareTheSamePhysicalRegister() {
        assertThat(Register.EAX.id()).isEqualTo(RegisterId.RAX);
        assertThat(Register.AL.id()).isEqualTo(RegisterId.RAX);
        assertThat(Register.ESI.bits()).isEqualTo(32);
    }

    @Test
    void maskMatchesWidth() {
        assertThat(Register.AL.mask()).isEqualTo(0xFFL);
        assertThat(Register.EAX.mask()).isEqualTo(0xFFFF_FFFFL);
        assertThat(Register.RAX.mask()).isEqualTo(-1L);
    }
}