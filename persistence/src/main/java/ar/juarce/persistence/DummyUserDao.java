package ar.juarce.persistence;

import ar.juarce.interfaces.UserDao;
import ar.juarce.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DummyUserDao implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(DummyUserDao.class);
    private final List<User> users;

    public DummyUserDao() {
        users = List.of(
                new User(1, "Julian", "julian@email.com", "1234"),
                new User(2, "Juan", "juan@email.com", "1234")
        );
    }

    @Override
    public List<User> findAll() {
        return users;
    }
}
