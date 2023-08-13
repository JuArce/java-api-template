package ar.juarce.interfaces;

import ar.juarce.models.User;

import java.util.List;

public interface UserDao {

    List<User> findAll();
}
