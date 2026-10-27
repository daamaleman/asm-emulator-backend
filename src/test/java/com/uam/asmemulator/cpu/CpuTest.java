package com.uam.asmemulator.cpu;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CpuTest {

    private final Cpu cpu = new Cpu();

    @Test
    void startsWithInitialStackPointer() {
        assertThat(cpu.registers().get(RegisterId.RSP)).isEqualTo(Cpu.INITIAL_STACK_POINTER);
    }

    @Test
    void pushDecrementsStackPointerBy8() {
        cpu.push(42);

        assertThat(cpu.registers().get(RegisterId.RSP)).isEqualTo(Cpu.INITIAL_STACK_POINTER - 8);
        assertThat(cpu.stack().read(Cpu.INITIAL_STACK_POINTER - 8)).isEqualTo(42L);
    }

    @Test
    void pushAndPopFollowLifoOrder() {
        cpu.push(1);
        cpu.push(2);

        assertThat(cpu.pop()).isEqualTo(2L);
        assertThat(cpu.pop()).isEqualTo(1L);
        assertThat(cpu.registers().get(RegisterId.RSP)).isEqualTo(Cpu.INITIAL_STACK_POINTER);
    }

    @Test
    void resetClearsHaltAndStack() {
        cpu.push(5);
        cpu.halt();

        cpu.reset();

        assertThat(cpu.halted()).isFalse();
        assertThat(cpu.stack().contents()).isEmpty();
    }
}

