package dev.pethaven.exception;

import dev.pethaven.dto.MessageResponse;
import io.minio.errors.MinioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IOException.class, NoSuchAlgorithmException.class})
    public ResponseEntity<String> handleIOException(IOException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("IOException occurred: " + ex.getMessage());
    }

    @ExceptionHandler(InvalidKeyException.class)
    public ResponseEntity<String> handleInvalidKeyException(InvalidKeyException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("InvalidKeyException occurred: " + ex.getMessage());
    }

    @ExceptionHandler(MinioException.class)
    public ResponseEntity<String> handleMinioException(MinioException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("MinioException occurred: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Exception occured: " + ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<MessageResponse> handleException(NotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new MessageResponse(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<MessageResponse> handleInvalidParameterException(InvalidParameterException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(exception.getMessage()));
    }
}
