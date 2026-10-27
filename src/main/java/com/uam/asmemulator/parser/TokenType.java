package com.uam.asmemulator.parser;

public enum TokenType {
    IDENTIFIER, // mnemónico, registro o etiqueta
    NUMBER,     // 5, -3, 0x1F, 0b101, 0FFh
    COMMA,
    COLON,
    NEWLINE,
    EOF
}