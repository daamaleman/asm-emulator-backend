package com.uam.asmemulator.parser;

import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.parser.model.ImmediateOperand;
import com.uam.asmemulator.parser.model.LabelOperand;
import com.uam.asmemulator.parser.model.Operand;
import com.uam.asmemulator.parser.model.ParsedInstruction;
import com.uam.asmemulator.parser.model.Program;
import com.uam.asmemulator.parser.model.RegisterOperand;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static com.uam.asmemulator.parser.TokenType.*;

/**
 * Analiza los tokens y construye el programa.
 *
 * Gramática (una instrucción por línea):
 *   linea       := [etiqueta ':'] [instruccion] NEWLINE
 *   instruccion := MNEMONICO [operando (',' operando)*]
 *   operando    := REGISTRO | NUMERO | ETIQUETA
 */
public class Parser {

    private final List<Token> tokens;
    private final List<String> sourceLines;
    private final List<ParsedInstruction> instructions = new ArrayList<>();
    private final Map<String, Integer> labels = new LinkedHashMap<>();
    private final List<Token> labelReferences = new ArrayList<>();
    private int current = 0;

    public Parser(List<Token> tokens, List<String> sourceLines) {
        this.tokens = tokens;
        this.sourceLines = sourceLines;
    }

    /** Punto de entrada: texto ensamblador → programa. */
    public static Program parse(String source) {
        String text = source == null ? "" : source;
        List<Token> tokens = new Lexer(text).tokenize();
        return new Parser(tokens, text.lines().toList()).parseProgram();
    }

    public Program parseProgram() {
        while (!check(EOF)) {
            if (match(NEWLINE)) {
                continue;
            }
            parseLine();
        }
        validateLabelReferences();
        return new Program(instructions, labels);
    }

    private void parseLine() {
        Token first = expect(IDENTIFIER, "Se esperaba una instrucción o etiqueta");

        if (match(COLON)) {
            defineLabel(first);
            if (check(NEWLINE) || check(EOF)) {
                return; // etiqueta sola en su línea
            }
            first = expect(IDENTIFIER, "Se esperaba una instrucción después de la etiqueta");
        }

        parseInstruction(first);

        if (!check(EOF)) {
            expect(NEWLINE, "Se esperaba ',' o fin de línea");
        }
    }

    private void defineLabel(Token token) {
        String name = token.text();
        if (Register.fromName(name).isPresent()) {
            throw error(token, "'" + name + "' es un registro y no puede usarse como etiqueta");
        }
        if (labels.containsKey(name)) {
            throw error(token, "La etiqueta '" + name + "' ya fue definida");
        }
        labels.put(name, instructions.size());
    }

    private void parseInstruction(Token mnemonicToken) {
        if (Register.fromName(mnemonicToken.text()).isPresent()) {
            throw error(mnemonicToken,
                    "Se esperaba una instrucción, pero '" + mnemonicToken.text() + "' es un registro");
        }
        String mnemonic = mnemonicToken.text().toUpperCase(Locale.ROOT);

        List<Operand> operands = new ArrayList<>();
        if (!check(NEWLINE) && !check(EOF)) {
            do {
                operands.add(parseOperand());
            } while (match(COMMA));
        }

        instructions.add(new ParsedInstruction(
                mnemonic, operands, mnemonicToken.line(), sourceText(mnemonicToken.line())));
    }

    private Operand parseOperand() {
        Token token = peek();
        if (match(NUMBER)) {
            return new ImmediateOperand(NumberLiteral.parse(token.text(), token.line(), token.column()));
        }
        if (match(IDENTIFIER)) {
            Optional<Register> register = Register.fromName(token.text());
            if (register.isPresent()) {
                return new RegisterOperand(register.get());
            }
            labelReferences.add(token);
            return new LabelOperand(token.text());
        }
        throw error(token, "Se esperaba un operando (registro, número o etiqueta)" + found(token));
    }

    private void validateLabelReferences() {
        for (Token reference : labelReferences) {
            if (!labels.containsKey(reference.text())) {
                throw error(reference, "La etiqueta '" + reference.text() + "' no está definida");
            }
        }
    }

    // ---- utilidades ----

    private String sourceText(int line) {
        return line - 1 < sourceLines.size() ? sourceLines.get(line - 1).strip() : "";
    }

    private Token peek() {
        return tokens.get(current);
    }

    private boolean check(TokenType type) {
        return peek().type() == type;
    }

    private boolean match(TokenType type) {
        if (check(type)) {
            current++;
            return true;
        }
        return false;
    }

    private Token expect(TokenType type, String message) {
        Token token = peek();
        if (match(type)) {
            return token;
        }
        throw error(token, message + found(token));
    }

    private static String found(Token token) {
        return switch (token.type()) {
            case EOF -> " (se encontró el fin del código)";
            case NEWLINE -> " (se encontró el fin de línea)";
            default -> " (se encontró '" + token.text() + "')";
        };
    }

    private static AsmSyntaxException error(Token token, String message) {
        return new AsmSyntaxException(message, token.line(), token.column());
    }
}