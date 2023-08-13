package ar.juarce.services;

import ar.juarce.interfaces.UserDao;
import ar.juarce.interfaces.UserService;
import ar.juarce.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DummyUserService implements UserService {

    private final UserDao userDao;

    @Autowired
    public DummyUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User create(User entity) {
        return userDao.create(entity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public User update(Long id, User entity) {
        return userDao.update(id, entity);
    }

    @Override
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

    @Override
    public void delete(User entity) {
        userDao.delete(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return userDao.existsById(id);
    }

    @Override
    public long count() {
        return userDao.count();
    }
}
