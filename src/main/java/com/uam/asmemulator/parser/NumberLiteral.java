package com.uam.asmemulator.parser;

import java.util.Locale;

/**
 * Convierte literales numéricos: decimal (-3), hexadecimal (0x1F o 1Fh)
 * y binario (0b101).
 */
final class NumberLiteral {

    private NumberLiteral() {
    }

    static long parse(String text, int line, int column) {
        String t = text.toLowerCase(Locale.ROOT);
        boolean negative = t.startsWith("-");
        String digits = negative ? t.substring(1) : t;
        try {
            long value;
            if (digits.startsWith("0x")) {
                value = Long.parseUnsignedLong(digits.substring(2), 16);
            } else if (digits.startsWith("0b")) {
                value = Long.parseUnsignedLong(digits.substring(2), 2);
            } else if (digits.endsWith("h")) {
                value = Long.parseUnsignedLong(digits.substring(0, digits.length() - 1), 16);
            } else {
                return Long.parseLong(t);
            }
            return negative ? -value : value;
        } catch (NumberFormatException e) {
            throw new AsmSyntaxException("Número inválido '" + text + "'", line, column);
        }
    }
}