package ar.juarce.interfaces.exceptions;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(Long id) {
        super(String.format("User with id %s", id));
    }
}
