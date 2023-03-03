package com.techelevator.tenmo.Service;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class RestUserService implements UserService {

    UserDao userDao;

    public RestUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getUsers() {
        return userDao.findAll();
    }
}
