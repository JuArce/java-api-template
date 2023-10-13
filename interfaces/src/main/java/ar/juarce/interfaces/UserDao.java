package ar.juarce.interfaces;

import ar.juarce.models.User;

import java.util.Optional;


public interface UserDao extends CrudOperations<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
