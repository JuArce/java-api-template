package ar.juarce.persistence;

import ar.juarce.interfaces.UserDao;
import ar.juarce.models.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DummyUserDao implements UserDao {

    private final List<User> users;

    public DummyUserDao() {
        users = new ArrayList<>();
        users.addAll(List.of(
                new User(1L, "Julian", "julian@email.com", "1234"),
                new User(2L, "Juan", "juan@email.com", "1234")
        ));
    }

    @Override
    public User create(User entity) {
        final Long id =(long) (users.size() + 1);
        final User user = new User(id, entity.getName(), entity.getEmail(), entity.getPassword());
        users.add(user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return users.stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public User update(Long id, User entity) {
        entity.setId(id);
        final User user = new User(id, entity.getName(), entity.getEmail(), entity.getPassword());
        users.set(users.indexOf(entity), user);
        return user;
    }

    @Override
    public void deleteById(Long id) {
        users.removeIf(user -> user.getId().equals(id));
    }

    @Override
    public void delete(User entity) {
        users.remove(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return users.stream().anyMatch(user -> user.getId().equals(id));
    }

    @Override
    public long count() {
        return users.size();
    }
}
