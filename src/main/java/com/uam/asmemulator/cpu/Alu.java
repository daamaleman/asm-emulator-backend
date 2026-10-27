package com.uam.asmemulator.cpu;

/**
 * Unidad aritmético-lógica: calcula resultados y actualiza las banderas.
 * Todos los valores se tratan con el ancho indicado (8, 16, 32 o 64 bits).
 */
public final class Alu {

    private Alu() {
    }

    /** ADD: CF si hay acarreo sin signo, OF si hay desbordamiento con signo. */
    public static long add(long a, long b, int bits, Flags flags) {
        long x = Bits.truncate(a, bits);
        long y = Bits.truncate(b, bits);
        long result = Bits.truncate(x + y, bits);

        boolean sx = Bits.signBit(x, bits);
        boolean sy = Bits.signBit(y, bits);
        boolean sr = Bits.signBit(result, bits);

        flags.setCf(Long.compareUnsigned(result, x) < 0);
        flags.setOf(sx == sy && sr != sx);
        flags.setZeroAndSign(result, bits);
        return result;
    }

    /** SUB y CMP: CF si hay préstamo (a < b sin signo). */
    public static long sub(long a, long b, int bits, Flags flags) {
        long x = Bits.truncate(a, bits);
        long y = Bits.truncate(b, bits);
        long result = Bits.truncate(x - y, bits);

        boolean sx = Bits.signBit(x, bits);
        boolean sy = Bits.signBit(y, bits);
        boolean sr = Bits.signBit(result, bits);

        flags.setCf(Long.compareUnsigned(x, y) < 0);
        flags.setOf(sx != sy && sr != sx);
        flags.setZeroAndSign(result, bits);
        return result;
    }

    /** INC: igual que ADD 1, pero no modifica CF. */
    public static long inc(long a, int bits, Flags flags) {
        boolean carry = flags.cf();
        long result = add(a, 1, bits, flags);
        flags.setCf(carry);
        return result;
    }

    /** DEC: igual que SUB 1, pero no modifica CF. */
    public static long dec(long a, int bits, Flags flags) {
        boolean carry = flags.cf();
        long result = sub(a, 1, bits, flags);
        flags.setCf(carry);
        return result;
    }

    /** NEG: 0 - a. CF=1 si a != 0; OF=1 si a es el mínimo negativo (INT_MIN). */
    public static long neg(long a, int bits, Flags flags) {
        return sub(0, a, bits, flags);
    }

    /** IMUL de dos operandos: CF=OF=1 si el producto no cabe en el ancho. */
    public static long imul(long a, long b, int bits, Flags flags) {
        long x = Bits.signExtend(Bits.truncate(a, bits), bits);
        long y = Bits.signExtend(Bits.truncate(b, bits), bits);

        long result;
        boolean overflow;
        if (bits == 64) {
            result = x * y;
            long high = Math.multiplyHigh(x, y);
            overflow = high != (result >> 63);
        } else {
            long product = x * y; // cabe en 64 bits para anchos <= 32
            result = Bits.truncate(product, bits);
            overflow = Bits.signExtend(result, bits) != product;
        }

        flags.setCf(overflow);
        flags.setOf(overflow);
        flags.setZeroAndSign(result, bits);
        return result;
    }

    public static long and(long a, long b, int bits, Flags flags) {
        return logic(a & b, bits, flags);
    }

    public static long or(long a, long b, int bits, Flags flags) {
        return logic(a | b, bits, flags);
    }

    public static long xor(long a, long b, int bits, Flags flags) {
        return logic(a ^ b, bits, flags);
    }

    /** NOT no modifica banderas. */
    public static long not(long a, int bits) {
        return Bits.truncate(~a, bits);
    }

    /** Las operaciones lógicas limpian CF y OF. */
    private static long logic(long value, int bits, Flags flags) {
        long result = Bits.truncate(value, bits);
        flags.setCf(false);
        flags.setOf(false);
        flags.setZeroAndSign(result, bits);
        return result;
    }
}