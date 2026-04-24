package fecaf.eduardo.gerenciador.handlers;

import fecaf.eduardo.gerenciador.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleBodyMissing(HttpMessageNotReadableException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Corpo da requisição é obrigatório");
        error.put("status", 400);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UnauthorizedExceptionHandler.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorized(UnauthorizedExceptionHandler ex) {
        return ResponseEntity.status(401).body(
                new ApiResponse<>(false, ex.getMessage(), null)
        );
    }
}