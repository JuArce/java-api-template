package ar.juarce.interfaces;

import ar.juarce.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends CrudOperations<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

}
