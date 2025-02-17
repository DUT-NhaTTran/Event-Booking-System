package com.tmnhat.userservice.controller;

import com.tmnhat.userservice.model.Users;
import com.tmnhat.userservice.service.Impl.UserServiceImpl;
import com.tmnhat.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(){
        this.userService= new UserServiceImpl();
    }

    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody Users user) throws SQLException {
        userService.addUser(user);
        return ResponseEntity.ok("User created");
    }




}
