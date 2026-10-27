package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Bits;
import com.uam.asmemulator.cpu.Cpu;
import com.uam.asmemulator.parser.model.ImmediateOperand;
import com.uam.asmemulator.parser.model.LabelOperand;
import com.uam.asmemulator.parser.model.Operand;
import com.uam.asmemulator.parser.model.RegisterOperand;

/**
 * Lee el valor de un operando con el ancho indicado.
 */
final class Operands {

    private Operands() {
    }

    static long read(Operand operand, Cpu cpu, int bits) {
        return switch (operand) {
            case RegisterOperand r -> cpu.registers().read(r.register());
            case ImmediateOperand i -> Bits.truncate(i.value(), bits);
            case LabelOperand l -> throw new IllegalStateException(
                    "La etiqueta '" + l.name() + "' no tiene un valor numérico");
        };
    }
}