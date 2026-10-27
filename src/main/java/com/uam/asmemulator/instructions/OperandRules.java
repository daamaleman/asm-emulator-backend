package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Bits;
import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.parser.AsmSyntaxException;
import com.uam.asmemulator.parser.model.ImmediateOperand;
import com.uam.asmemulator.parser.model.LabelOperand;
import com.uam.asmemulator.parser.model.Operand;
import com.uam.asmemulator.parser.model.ParsedInstruction;
import com.uam.asmemulator.parser.model.RegisterOperand;
import com.uam.asmemulator.parser.model.Program;

/**
 * Validaciones comunes de operandos, con mensajes claros para el estudiante.
 */
final class OperandRules {

    private OperandRules() {
    }

    static void expectCount(ParsedInstruction p, int expected) {
        int actual = p.operands().size();
        if (actual != expected) {
            throw error(p, "%s espera %d operando%s, pero recibió %d"
                    .formatted(p.mnemonic(), expected, expected == 1 ? "" : "s", actual));
        }
    }

    /** El primer operando (destino) debe ser un registro. */
    static Register destination(ParsedInstruction p) {
        Operand operand = p.operands().get(0);
        if (operand instanceof RegisterOperand r) {
            return r.register();
        }
        throw error(p, "El destino de %s debe ser un registro (se encontró %s)"
                .formatted(p.mnemonic(), describe(operand)));
    }

    /** El segundo operando (fuente) debe ser compatible con el destino. */
    static Operand source(ParsedInstruction p, Register destination) {
        Operand operand = p.operands().get(1);
        switch (operand) {
            case RegisterOperand r -> {
                if (r.register().bits() != destination.bits()) {
                    throw error(p, "Los operandos de %s tienen tamaños distintos: %s (%d bits) y %s (%d bits)"
                            .formatted(p.mnemonic(), destination, destination.bits(),
                                    r.register(), r.register().bits()));
                }
            }
            case ImmediateOperand i -> {
                if (!Bits.fits(i.value(), destination.bits())) {
                    throw error(p, "El valor %d no cabe en %s (%d bits)"
                            .formatted(i.value(), destination, destination.bits()));
                }
            }
            case LabelOperand l -> throw error(p, "La etiqueta '%s' no puede usarse como operando de %s"
                    .formatted(l.name(), p.mnemonic()));
        }
        return operand;
    }

    /** El único operando debe ser una etiqueta; devuelve el índice destino. */
    static int jumpTarget(ParsedInstruction p, Program program) {
        expectCount(p, 1);
        Operand operand = p.operands().get(0);
        if (operand instanceof LabelOperand l) {
            return program.addressOf(l.name());
        }
        throw error(p, "%s requiere una etiqueta como destino (se encontró %s)"
                .formatted(p.mnemonic(), describe(operand)));
    }

    /** El primer operando debe ser un registro de 64 bits (PUSH/POP). */
    static Register register64(ParsedInstruction p) {
        Register register = destination(p);
        if (register.bits() != 64) {
            throw error(p, "%s solo admite registros de 64 bits (se encontró %s)"
                    .formatted(p.mnemonic(), register));
        }
        return register;
    }


    static String describe(Operand operand) {
        return switch (operand) {
            case RegisterOperand r -> "el registro " + r.register();
            case ImmediateOperand i -> "el número " + i.value();
            case LabelOperand l -> "la etiqueta '" + l.name() + "'";
        };
    }

    static AsmSyntaxException error(ParsedInstruction p, String message) {
        return new AsmSyntaxException(message, p.line(), 1);
    }
}