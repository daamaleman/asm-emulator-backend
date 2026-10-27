package com.uam.asmemulator.emulator;

import com.uam.asmemulator.cpu.Flags;

public record FlagsSnapshot(boolean zf, boolean sf, boolean cf, boolean of) {

    public static FlagsSnapshot of(Flags flags) {
        return new FlagsSnapshot(flags.zf(), flags.sf(), flags.cf(), flags.of());
    }
}

