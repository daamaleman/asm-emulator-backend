package com.uam.asmemulator.parser;

/**
 * Error de sintaxis en el código ensamblador, con su posición.
 */
public class AsmSyntaxException extends RuntimeException {

    private final int line;
    private final int column;
    private final String detail;

    public AsmSyntaxException(String detail, int line, int column) {
        super("Línea %d, columna %d: %s".formatted(line, column, detail));
        this.line = line;
        this.column = column;
        this.detail = detail;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    /** Mensaje sin la posición, útil para mostrarlo en la interfaz. */
    public String getDetail() {
        return detail;
    }
}