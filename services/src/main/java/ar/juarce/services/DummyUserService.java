package ar.juarce.services;

import ar.juarce.interfaces.UserDao;
import ar.juarce.interfaces.UserService;
import ar.juarce.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DummyUserService implements UserService {

    private final UserDao userDao;

    @Autowired
    public DummyUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }
}
