package com.uam.asmemulator.api.dto;

/**
 * @param line   línea del error en el código (null si no aplica)
 * @param column columna del error (null si no aplica)
 */
public record ErrorResponse(String error, String message, Integer line, Integer column) {

    public static ErrorResponse of(String error, String message) {
        return new ErrorResponse(error, message, null, null);
    }
}

