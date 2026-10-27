package com.uam.asmemulator.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.uam.asmemulator.parser.TokenType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LexerTest {

    private static List<TokenType> types(String source) {
        return new Lexer(source).tokenize().stream().map(Token::type).toList();
    }

    @Test
    void tokenizesInstructionAndIgnoresComment() {
        assertThat(types("mov eax, edi ; eax = a"))
                .containsExactly(IDENTIFIER, IDENTIFIER, COMMA, IDENTIFIER, EOF);
    }

    @Test
    void tokenizesLabelDefinition() {
        assertThat(types("inicio:")).containsExactly(IDENTIFIER, COLON, EOF);
    }

    @Test
    void tokenizesNumericLiterals() {
        List<Token> tokens = new Lexer("-3 0x1F 0b101 0FFh").tokenize();
        assertThat(tokens.stream().map(Token::text).toList())
                .containsExactly("-3", "0x1F", "0b101", "0FFh", "");
        assertThat(tokens.stream().map(Token::type).toList())
                .containsExactly(NUMBER, NUMBER, NUMBER, NUMBER, EOF);
    }

    @Test
    void tracksLinesAndColumns() {
        List<Token> tokens = new Lexer("mov eax, 1\n  add eax, 2").tokenize();
        Token add = tokens.stream().filter(t -> t.text().equals("add")).findFirst().orElseThrow();
        assertThat(add.line()).isEqualTo(2);
        assertThat(add.column()).isEqualTo(3);
    }

    @Test
    void rejectsMemoryOperands() {
        AsmSyntaxException ex = assertThrows(AsmSyntaxException.class,
                () -> new Lexer("mov eax, [rbx]").tokenize());
        assertThat(ex.getLine()).isEqualTo(1);
        assertThat(ex.getColumn()).isEqualTo(10);
    }

    @Test
    void rejectsUnexpectedCharacter() {
        assertThrows(AsmSyntaxException.class, () -> new Lexer("mov eax, $5").tokenize());
    }
}