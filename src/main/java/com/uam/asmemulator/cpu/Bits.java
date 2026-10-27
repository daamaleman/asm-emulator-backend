package com.uam.asmemulator.cpu;

/**
 * Operaciones sobre valores de 8, 16, 32 o 64 bits guardados en un long.
 */
public final class Bits {

    private Bits() {
    }

    /** Máscara con los bits del ancho dado: mask(8) = 0xFF. */
    public static long mask(int bits) {
        return bits == 64 ? -1L : (1L << bits) - 1;
    }

    /** Se queda solo con los bits del ancho dado. */
    public static long truncate(long value, int bits) {
        return value & mask(bits);
    }

    /** Interpreta el valor como entero con signo (complemento a dos). */
    public static long signExtend(long value, int bits) {
        int shift = 64 - bits;
        return (value << shift) >> shift;
    }

    /** Bit de signo (el más significativo) del ancho dado. */
    public static boolean signBit(long value, int bits) {
        return ((value >>> (bits - 1)) & 1L) == 1L;
    }

    /** Indica si el valor cabe en el ancho, como número con signo o sin signo. */
    public static boolean fits(long value, int bits) {
        if (bits == 64) {
            return true;
        }
        long min = -(1L << (bits - 1));
        long max = (1L << bits) - 1;
        return value >= min && value <= max;
    }
}