package com.rental.dao;

import com.rental.model.user.User;
import java.sql.SQLException;
import java.util.List;

public interface UserDAOInterface {
    
    
    User login(String email, String password) throws SQLException;
    
    
    void save(User user) throws SQLException;
    void update(User user) throws SQLException;
    void delete(int id, String role) throws SQLException;
    
   
    User findById(int id, String role) throws SQLException;
    List<User> findAll() throws SQLException;
}