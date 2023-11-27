package dev.pethaven.exception;

public class InvalidPetStatusException extends RuntimeException{
    public InvalidPetStatusException(String message) {
        super(message);
    }
}
