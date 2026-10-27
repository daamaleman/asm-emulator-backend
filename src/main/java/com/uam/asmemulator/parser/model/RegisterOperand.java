package com.uam.asmemulator.parser.model;

import com.uam.asmemulator.cpu.Register;

public record RegisterOperand(Register register) implements Operand {

}