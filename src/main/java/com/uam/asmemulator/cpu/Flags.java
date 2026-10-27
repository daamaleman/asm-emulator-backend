package com.uam.asmemulator.cpu;

/**
 * Banderas de estado usadas en las comparaciones (subconjunto de RFLAGS).
 */
public class Flags {

    private boolean zf; // Zero: el resultado es cero
    private boolean sf; // Sign: bit de signo del resultado
    private boolean cf; // Carry: acarreo/préstamo sin signo
    private boolean of; // Overflow: desbordamiento con signo

    public boolean zf() { return zf; }
    public boolean sf() { return sf; }
    public boolean cf() { return cf; }
    public boolean of() { return of; }

    public void setZf(boolean value) { zf = value; }
    public void setSf(boolean value) { sf = value; }
    public void setCf(boolean value) { cf = value; }
    public void setOf(boolean value) { of = value; }

    /** Actualiza ZF y SF a partir de un resultado del ancho dado. */
    public void setZeroAndSign(long result, int bits) {
        zf = Bits.truncate(result, bits) == 0;
        sf = Bits.signBit(result, bits);
    }

    public void reset() {
        zf = sf = cf = of = false;
    }

    @Override
    public String toString() {
        return "ZF=%d SF=%d CF=%d OF=%d".formatted(bit(zf), bit(sf), bit(cf), bit(of));
    }

    private static int bit(boolean b) {
        return b ? 1 : 0;
    }
}