package com.uam.asmemulator.api;

import com.uam.asmemulator.api.dto.ErrorResponse;
import com.uam.asmemulator.parser.AsmSyntaxException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce excepciones a respuestas 400 con un formato común.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AsmSyntaxException.class)
    public ResponseEntity<ErrorResponse> handleSyntax(AsmSyntaxException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("SYNTAX_ERROR", e.getDetail(), e.getLine(), e.getColumn()));
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequest(InvalidRequestException e) {
        return ResponseEntity.badRequest().body(ErrorResponse.of("INVALID_REQUEST", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Petición inválida");
        return ResponseEntity.badRequest().body(ErrorResponse.of("VALIDATION_ERROR", message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of("INVALID_JSON", "El cuerpo de la petición no es un JSON válido"));
    }
}