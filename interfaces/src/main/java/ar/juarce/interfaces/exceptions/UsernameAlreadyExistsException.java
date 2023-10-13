package ar.juarce.interfaces.exceptions;

public class UsernameAlreadyExistsException extends AlreadyExistsException {

        public UsernameAlreadyExistsException() {
            super("Username already exists");
        }
}
