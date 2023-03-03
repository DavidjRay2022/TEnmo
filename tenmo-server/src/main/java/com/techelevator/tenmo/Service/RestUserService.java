package com.techelevator.tenmo.Service;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;


import java.util.List;

public class RestUserService implements UserService {

    UserDao userDao;

    public List<User> getUsers() {
        return userDao.findAll();
    }
}
