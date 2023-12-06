package dev.pethaven.exception;

import dev.pethaven.dto.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
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

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<MessageResponse> handleAlreadyExistsException(AlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(UntraceableMinioException.class)
    public ResponseEntity<MessageResponse> handleMyMinioException(UntraceableMinioException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidPetStatusException.class)
    public ResponseEntity<MessageResponse> handleMyMinioException(InvalidPetStatusException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidChatException.class)
    public ResponseEntity<MessageResponse> handleInvalidChatException(InvalidChatException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new MessageResponse(ex.getMessage()));
    }
}
