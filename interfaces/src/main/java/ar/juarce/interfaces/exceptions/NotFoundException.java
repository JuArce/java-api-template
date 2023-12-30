package ar.juarce.interfaces.exceptions;

public class NotFoundException extends Exception {

    public NotFoundException(String message) {
        super(String.format("%s was not found", message));
    }
}
