package ru.javawebinar.topjava.util.exception;

public class DuplicateValueException extends RuntimeException{
    public String type;
    public DuplicateValueException(String message, String type) {
        super(message);
        this.type = type;
    }
}
