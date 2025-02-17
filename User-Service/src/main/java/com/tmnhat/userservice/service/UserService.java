package com.tmnhat.userservice.service;

import com.tmnhat.userservice.model.Users;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    void addUser(Users user) throws SQLException;
    void updateUser(Long id,Users user) throws SQLException;
    Users getUserById(Long id) throws SQLException;
    List<Users> getAllUsers() throws SQLException;
}
