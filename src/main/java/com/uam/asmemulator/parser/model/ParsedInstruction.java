package com.uam.asmemulator.parser.model;

import java.util.List;

/**
 * Instrucción ya reconocida por el parser.
 *
 * @param mnemonic   en mayúsculas (MOV, ADD, JGE...)
 * @param operands   en orden Intel: destino primero
 * @param line       línea del código fuente (desde 1)
 * @param sourceText texto original de la línea, para la traza y la traducción
 */
public record ParsedInstruction(String mnemonic, List<Operand> operands, int line, String sourceText) {

    public ParsedInstruction {
        operands = List.copyOf(operands);
    }
}