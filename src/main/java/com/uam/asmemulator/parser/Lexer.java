package com.uam.asmemulator.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntPredicate;

/**
 * Convierte el texto ensamblador en una lista de tokens.
 * Ignora espacios y comentarios (desde ';' hasta el fin de línea).
 */
public class Lexer {

    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int pos = 0;
    private int line = 1;
    private int column = 1;

    public Lexer(String source) {
        this.source = source == null ? "" : source;
    }

    public List<Token> tokenize() {
        while (!isAtEnd()) {
            char c = peek();
            switch (c) {
                case ' ', '\t', '\r' -> advance();
                case '\n' -> addSingle(TokenType.NEWLINE);
                case ';' -> skipComment();
                case ',' -> addSingle(TokenType.COMMA);
                case ':' -> addSingle(TokenType.COLON);
                case '[' -> throw new AsmSyntaxException(
                        "El acceso a memoria ([...]) aún no está soportado", line, column);
                default -> {
                    if (Character.isDigit(c) || (c == '-' && Character.isDigit(peekNext()))) {
                        readToken(TokenType.NUMBER, Character::isLetterOrDigit);
                    } else if (isIdentifierStart(c)) {
                        readToken(TokenType.IDENTIFIER, Lexer::isIdentifierPart);
                    } else {
                        throw new AsmSyntaxException("Carácter inesperado '" + c + "'", line, column);
                    }
                }
            }
        }
        tokens.add(new Token(TokenType.EOF, "", line, column));
        return List.copyOf(tokens);
    }

    private void addSingle(TokenType type) {
        tokens.add(new Token(type, String.valueOf(peek()), line, column));
        advance();
    }

    /** Lee un token cuyo primer carácter ya fue validado. */
    private void readToken(TokenType type, IntPredicate isPart) {
        int startLine = line;
        int startColumn = column;
        int start = pos;
        advance();
        while (!isAtEnd() && isPart.test(peek())) {
            advance();
        }
        tokens.add(new Token(type, source.substring(start, pos), startLine, startColumn));
    }

    private void skipComment() {
        while (!isAtEnd() && peek() != '\n') {
            advance();
        }
    }

    private char advance() {
        char c = source.charAt(pos++);
        if (c == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }
        return c;
    }

    private char peek() {
        return source.charAt(pos);
    }

    private char peekNext() {
        return pos + 1 < source.length() ? source.charAt(pos + 1) : '\0';
    }

    private boolean isAtEnd() {
        return pos >= source.length();
    }

    private static boolean isIdentifierStart(int c) {
        return Character.isLetter(c) || c == '_' || c == '.';
    }

    private static boolean isIdentifierPart(int c) {
        return Character.isLetterOrDigit(c) || c == '_' || c == '.';
    }
}