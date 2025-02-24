package com.tmnhat.userservice.controller;

import com.tmnhat.userservice.model.Users;
import com.tmnhat.userservice.service.Impl.UserServiceImpl;
import com.tmnhat.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(){
        this.userService= new UserServiceImpl();
    }

    @PostMapping()
    public ResponseEntity<String> addUser(@RequestBody Users user) throws SQLException {
        userService.addUser(user);
        return ResponseEntity.ok("User created");
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") Long id, @RequestBody Users user) throws SQLException {
        userService.updateUser(id,user);
        return ResponseEntity.ok("User updated");
    }
    @GetMapping()
    public ResponseEntity<List<Users>> getAllUsers() throws SQLException{
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable("id") Long id ) throws SQLException {
        return ResponseEntity.ok(userService.getUserById(id));
    }



}
