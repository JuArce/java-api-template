package ar.juarce.persistence;

import ar.juarce.interfaces.UserDao;
import ar.juarce.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DummyUserDao implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User create(User entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));

    }

    // TODO
    @Override
    public List<User> findAll() {
//        entityManager.getCriteriaBuilder()
//                .createQuery(User.class)
//                .from(User.class);
        return new ArrayList<>();
    }

    @Override
    public User update(Long id, User entity) {
        entity.setId(id);
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(findById(id));
    }

    @Override
    public void delete(User entity) {
        entityManager.remove(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    // TODO
    @Override
    public long count() {
        return 0;
    }
}
