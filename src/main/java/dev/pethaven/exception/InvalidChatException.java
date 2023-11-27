package dev.pethaven.exception;

public class InvalidChatException extends RuntimeException{
    public InvalidChatException(String message) {
        super(message);
    }
}
