package ru.DmN.bpl.exceptions;

public class NotProcessedInsertionException extends RuntimeException {
    public NotProcessedInsertionException(String text) {
        super(text);
    }
}
