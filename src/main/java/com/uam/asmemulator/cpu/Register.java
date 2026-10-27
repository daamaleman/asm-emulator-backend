package com.uam.asmemulator.cpu;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Nombres de registro que puede usar el programa. Cada nombre es una vista
 * (64/32/16/8 bits) de un registro físico: EAX es la mitad baja de RAX.
 */
public enum Register {
    RAX(RegisterId.RAX, 64), EAX(RegisterId.RAX, 32), AX(RegisterId.RAX, 16), AL(RegisterId.RAX, 8),
    RBX(RegisterId.RBX, 64), EBX(RegisterId.RBX, 32), BX(RegisterId.RBX, 16), BL(RegisterId.RBX, 8),
    RCX(RegisterId.RCX, 64), ECX(RegisterId.RCX, 32), CX(RegisterId.RCX, 16), CL(RegisterId.RCX, 8),
    RDX(RegisterId.RDX, 64), EDX(RegisterId.RDX, 32), DX(RegisterId.RDX, 16), DL(RegisterId.RDX, 8),
    RSI(RegisterId.RSI, 64), ESI(RegisterId.RSI, 32), SI(RegisterId.RSI, 16), SIL(RegisterId.RSI, 8),
    RDI(RegisterId.RDI, 64), EDI(RegisterId.RDI, 32), DI(RegisterId.RDI, 16), DIL(RegisterId.RDI, 8),
    RBP(RegisterId.RBP, 64), EBP(RegisterId.RBP, 32), BP(RegisterId.RBP, 16), BPL(RegisterId.RBP, 8),
    RSP(RegisterId.RSP, 64), ESP(RegisterId.RSP, 32), SP(RegisterId.RSP, 16), SPL(RegisterId.RSP, 8),
    R8(RegisterId.R8, 64), R8D(RegisterId.R8, 32), R8W(RegisterId.R8, 16), R8B(RegisterId.R8, 8),
    R9(RegisterId.R9, 64), R9D(RegisterId.R9, 32), R9W(RegisterId.R9, 16), R9B(RegisterId.R9, 8),
    R10(RegisterId.R10, 64), R10D(RegisterId.R10, 32), R10W(RegisterId.R10, 16), R10B(RegisterId.R10, 8),
    R11(RegisterId.R11, 64), R11D(RegisterId.R11, 32), R11W(RegisterId.R11, 16), R11B(RegisterId.R11, 8),
    R12(RegisterId.R12, 64), R12D(RegisterId.R12, 32), R12W(RegisterId.R12, 16), R12B(RegisterId.R12, 8),
    R13(RegisterId.R13, 64), R13D(RegisterId.R13, 32), R13W(RegisterId.R13, 16), R13B(RegisterId.R13, 8),
    R14(RegisterId.R14, 64), R14D(RegisterId.R14, 32), R14W(RegisterId.R14, 16), R14B(RegisterId.R14, 8),
    R15(RegisterId.R15, 64), R15D(RegisterId.R15, 32), R15W(RegisterId.R15, 16), R15B(RegisterId.R15, 8);

    private static final Map<String, Register> BY_NAME = Arrays.stream(values())
            .collect(Collectors.toUnmodifiableMap(Enum::name, Function.identity()));

    private final RegisterId id;
    private final int bits;

    Register(RegisterId id, int bits) {
        this.id = id;
        this.bits = bits;
    }

    public RegisterId id() {
        return id;
    }

    public int bits() {
        return bits;
    }

    /** Máscara con los bits que ocupa esta vista (p. ej. 0xFF para AL). */
    public long mask() {
        return bits == 64 ? -1L : (1L << bits) - 1;
    }

    /** Busca un registro por nombre, sin distinguir mayúsculas. */
    public static Optional<Register> fromName(String name) {
        return Optional.ofNullable(BY_NAME.get(name.toUpperCase(Locale.ROOT)));
    }
}