package ar.juarce.persistence;

import ar.juarce.interfaces.UserDao;
import ar.juarce.interfaces.exceptions.AlreadyExistsException;
import ar.juarce.interfaces.exceptions.EmailAlreadyExistsException;
import ar.juarce.interfaces.exceptions.UsernameAlreadyExistsException;
import ar.juarce.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserHibernateDao implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User create(User entity) throws AlreadyExistsException {
        validateUserUniqueness(entity);

        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));

    }

    // Refactor this
    @Override
    public List<User> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);

        criteriaQuery.select(root);

        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public User update(Long id, User entity) throws AlreadyExistsException {
        Optional<User> optionalUser = findById(id);

        if (optionalUser.isPresent()) {
            User user = updateUser(optionalUser.get(), entity);
            entityManager.persist(user);
            return user;
        } else {
            return create(entity);
        }
    }

    @Override
    public void deleteById(Long id) {
        entityManager.createNativeQuery("DELETE FROM users WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void delete(User entity) {
        deleteById(entity.getId());
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    // TODO
    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(*) FROM User", Long.class)
                .getSingleResult();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        final TypedQuery<User> query = entityManager.createQuery("FROM User WHERE email = :email", User.class)
                .setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        final TypedQuery<User> query = entityManager.createQuery("FROM User WHERE username = :username", User.class)
                .setParameter("username", username);
        return query.getResultList().stream().findFirst();
    }


    /*
    Auxiliary methods
     */
    private void validateUserUniqueness(User user) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        checkDuplicateUsername(user);
        checkDuplicateEmail(user);
    }

    private User updateUser(User user, User newValues) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        newValues.setId(user.getId());
        validateUserUniqueness(newValues);

        user.setUsername(newValues.getUsername());
        user.setEmail(newValues.getEmail());
        user.setPassword(newValues.getPassword());
        return user;
    }

    private void checkDuplicateUsername(User user) throws UsernameAlreadyExistsException {
        Optional<User> existingUser = findByUsername(user.getUsername());
        if (existingUser.isPresent() && isDifferentUser(existingUser.get(), user)) {
            throw new UsernameAlreadyExistsException();
        }
    }

    private void checkDuplicateEmail(User user) throws EmailAlreadyExistsException {
        Optional<User> existingUser = findByEmail(user.getEmail());
        if (existingUser.isPresent() && isDifferentUser(existingUser.get(), user)) {
            throw new EmailAlreadyExistsException();
        }
    }

    private boolean isDifferentUser(User user, User otherUser) {
        return !user.equals(otherUser);
    }
}
