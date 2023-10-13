package ar.juarce.interfaces.exceptions;

public class EmailAlreadyExistsException extends AlreadyExistsException {

    public EmailAlreadyExistsException() {
        super("Email already exists");
    }
}
