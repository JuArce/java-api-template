package ar.juarce.services;

import ar.juarce.interfaces.UserService;
import ar.juarce.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DummyUserService implements UserService {

    @Override
    public List<User> getUsers() {
        return List.of(
                new User(1, "Julian", "julian@email.com", "1234"),
                new User(2, "Juan", "juan@email.com", "1234")
        );
    }
}
