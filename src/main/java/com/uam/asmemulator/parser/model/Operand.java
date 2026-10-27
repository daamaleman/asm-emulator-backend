package com.uam.asmemulator.parser.model;

/** Operando de una instrucción: registro, número inmediato o etiqueta. */
public sealed interface Operand permits RegisterOperand, ImmediateOperand, LabelOperand {
}