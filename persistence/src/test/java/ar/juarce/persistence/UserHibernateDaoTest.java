package ar.juarce.persistence;

import ar.juarce.interfaces.exceptions.AlreadyExistsException;
import ar.juarce.interfaces.exceptions.EmailAlreadyExistsException;
import ar.juarce.interfaces.exceptions.UsernameAlreadyExistsException;
import ar.juarce.models.User;
import ar.juarce.persistence.config.TestConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.util.List;
import java.util.Optional;

import static ar.juarce.persistence.InstanceProvider.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
public class UserHibernateDaoTest {

    private static final String USER_TABLE = "users";

    private final JdbcTemplate jdbcTemplate;
    private final UserHibernateDao userHibernateDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserHibernateDaoTest(DataSource dataSource, UserHibernateDao userHibernateDao) {
        this.userHibernateDao = userHibernateDao;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    @DisplayName("Create user should success")
    public void createUser() throws AlreadyExistsException {
        final User newUser = User.builder()
                .email(USER_NEW_EMAIL)
                .username(USER_NEW_USERNAME)
                .password("123456")
                .build();


        final User user = userHibernateDao.create(newUser);

        entityManager.flush();

        assertNotNull(user.getId());
        assertEquals(USER_NEW_EMAIL, user.getEmail());
        assertEquals(USER_NEW_USERNAME, user.getUsername());
        assertEquals(1,
                JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE,
                        String.format("email = '%s' AND username = '%s'", USER_NEW_EMAIL, USER_NEW_USERNAME)
                )
        );
    }

    @Test
    @DisplayName("Create user with existing email should fail")
    public void createUserWithExistingEmail() {
        final User newUser = User.builder()
                .email(USER_1_EMAIL)
                .username(USER_NEW_USERNAME)
                .password("123456")
                .build();

        assertThrows(EmailAlreadyExistsException.class,
                () -> userHibernateDao.create(newUser));
    }

    @Test
    @DisplayName("Create user with existing username should fail")
    public void createUserWithExistingUsername() {
        final User newUser = User.builder()
                .email(USER_NEW_EMAIL)
                .username(USER_1_USERNAME)
                .password("123456")
                .build();

        assertThrows(UsernameAlreadyExistsException.class,
                () -> userHibernateDao.create(newUser));
    }

    @Test
    @DisplayName("Find user by id should success")
    public void findUserById() {
        final Optional<User> user = userHibernateDao.findById(USER_1_ID);

        assertTrue(user.isPresent());
        assertEquals(USER_1_ID, user.get().getId());
        assertEquals(USER_1_EMAIL, user.get().getEmail());
        assertEquals(USER_1_USERNAME, user.get().getUsername());
    }

    @Test
    @DisplayName("Find user by id should return empty optional")
    public void findUserByIdNotFound() {
        final Optional<User> user = userHibernateDao.findById(100L);

        assertTrue(user.isEmpty());
    }

    @Test
    @DisplayName("Find all users should success and return 2 users")
    public void findAllUsers() {
        final List<User> users = userHibernateDao.findAll();

        assertEquals(2, users.size());
    }

    //TODO test update

    @Test
    @DisplayName("Delete user 1 by id should success")
    public void deleteUserById() {
        userHibernateDao.deleteById(USER_1_ID);

        entityManager.flush();

        assertEquals(0,
                JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE,
                        String.format("id = %d", USER_1_ID)
                )
        );
    }

    @Test
    @DisplayName("Delete user 1 should success")
    public void deleteUser() {
        User user = user1();

        userHibernateDao.delete(user);

        entityManager.flush();

        assertEquals(0,
                JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE,
                        String.format("id = %d", USER_1_ID)
                )
        );
    }

    @Test
    @DisplayName("User 1 should exists")
    public void user1Exists() {
        final boolean exists = userHibernateDao.existsById(USER_1_ID);

        assertTrue(exists);
    }

    @Test
    @DisplayName("User 100 should not exist")
    public void user100NotExists() {
        final boolean exists = userHibernateDao.existsById(100L);

        assertFalse(exists);
    }

    @Test
    @DisplayName("Count users should return 2")
    public void countUsers() {
        final long count = userHibernateDao.count();

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Find by email should return user 1")
    public void findByEmail() {
        final Optional<User> user = userHibernateDao.findByEmail(USER_1_EMAIL);

        assertTrue(user.isPresent());
        assertEquals(USER_1_ID, user.get().getId());
        assertEquals(USER_1_EMAIL, user.get().getEmail());
        assertEquals(USER_1_USERNAME, user.get().getUsername());
    }

    @Test
    @DisplayName("Find by email should return empty optional")
    public void findByEmailNotFound() {
        final Optional<User> user = userHibernateDao.findByEmail(USER_NEW_EMAIL);

        assertTrue(user.isEmpty());
    }

    @Test
    @DisplayName("Find by username should return user 1")
    public void findByUsername() {
        final Optional<User> user = userHibernateDao.findByUsername(USER_1_USERNAME);

        assertTrue(user.isPresent());
        assertEquals(USER_1_ID, user.get().getId());
        assertEquals(USER_1_EMAIL, user.get().getEmail());
        assertEquals(USER_1_USERNAME, user.get().getUsername());
    }

    @Test
    @DisplayName("Find by username should return empty optional")
    public void findByUsernameNotFound() {
        final Optional<User> user = userHibernateDao.findByUsername(USER_NEW_USERNAME);

        assertTrue(user.isEmpty());
    }
}
