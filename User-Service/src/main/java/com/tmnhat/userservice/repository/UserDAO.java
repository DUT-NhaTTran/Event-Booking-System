package com.tmnhat.userservice.repository;

import com.tmnhat.userservice.model.Users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends BaseDAO {
    public void addUser(Users user) throws SQLException {
        String addUser="INSERT INTO users (name,email,phone_number,created_at) VALUES(?,?,?,?)";
        executeUpdate(addUser,stmt->{
            stmt.setString(1,user.getName());
            stmt.setString(2,user.getEmail());
            stmt.setString(3,user.getPhoneNumber());
            stmt.setTimestamp(4, user.getCreatedAt() != null ? Timestamp.valueOf(user.getCreatedAt()) : null);

        });
    }
    public void updateUser(Long id,Users user) throws SQLException {
        String updateUser="UPDATE users SET name=?,email=?,phone_number=?,created_at=? WHERE id=?";
        executeUpdate(updateUser,stmt->{
            stmt.setString(1,user.getName());
            stmt.setString(2,user.getEmail());
            stmt.setString(3,user.getPhoneNumber());
            stmt.setTimestamp(4, user.getCreatedAt() != null ? Timestamp.valueOf(user.getCreatedAt()) : null);
            stmt.setLong(5, id);
        });
    }
    public List<Users> getAllUsers() throws SQLException {

        String getAllUsers="SELECT * FROM users";
        return executeQuery(getAllUsers,stmt->{
            List<Users> usersList =new ArrayList<>();
            ResultSet rs=stmt.executeQuery();
            while(rs.next()){
                Users user=new Users.Builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .email(rs.getString("email"))
                        .phoneNumber(rs.getString("phone_number"))
                        .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                        .build();
                usersList.add(user);
            }
            return usersList;
        });
    }
    public Users getUserById(Long id) throws SQLException {
        String getUserById="SELECT * FROM users WHERE id=?";
        return executeQuery(getUserById,stmt->{
            ResultSet rs=stmt.executeQuery();
            if(rs.next()){
                return new Users.Builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .email(rs.getString("email"))
                        .phoneNumber(rs.getString("phone_number"))
                        .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                        .build();
            }
            return null;
        });
    }
}
