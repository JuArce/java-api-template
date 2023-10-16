package ar.juarce.services;

import ar.juarce.interfaces.UserDao;
import ar.juarce.interfaces.UserService;
import ar.juarce.interfaces.exceptions.AlreadyExistsException;
import ar.juarce.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    @Override
    public User create(User entity) throws AlreadyExistsException {
        return userDao.create(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Transactional
    @Override
    public User update(Long id, User entity) throws AlreadyExistsException {
        return userDao.update(id, entity);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

    @Transactional
    @Override
    public void delete(User entity) {
        userDao.delete(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long id) {
        return userDao.existsById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public long count() {
        return userDao.count();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
