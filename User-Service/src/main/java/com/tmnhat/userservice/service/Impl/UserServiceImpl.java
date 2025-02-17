package com.tmnhat.userservice.service.Impl;

import com.tmnhat.userservice.model.Users;
import com.tmnhat.userservice.repository.UserDAO;
import com.tmnhat.userservice.service.UserService;

import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDAO userDAO = new UserDAO();

    @Override
    public void addUser(Users user) throws SQLException {
        userDAO.addUser(user);
    }

    @Override
    public void updateUser(Long id, Users user) throws SQLException {
        userDAO.updateUser(id, user);
    }

    @Override
    public Users getUserById(Long id) throws SQLException {
        return userDAO.getUserById(id);
    }

    @Override
    public List<Users> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

}
