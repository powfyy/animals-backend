package dev.pethaven.exception;

public class UntraceableMinioException extends RuntimeException {
    public UntraceableMinioException(String message) {
        super(message);
    }
}
