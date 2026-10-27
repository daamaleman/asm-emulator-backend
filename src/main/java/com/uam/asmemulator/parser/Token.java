package com.uam.asmemulator.parser;

public record Token(TokenType type, String text, int line, int column) {

}