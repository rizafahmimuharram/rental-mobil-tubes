package com.rental.dao;

import com.rental.model.user.User;
import java.sql.SQLException;
import java.util.List;

public interface UserDAOInterface {
    
    // Metode untuk otentikasi
    User login(String email, String password) throws SQLException;
    
    // Metode CRUD Dasar
    void save(User user) throws SQLException;
    void update(User user) throws SQLException;
    void delete(int id, String role) throws SQLException;// Pastikan ada String role
    
    // Metode Pencarian
    User findById(int id, String role) throws SQLException;
    List<User> findAll() throws SQLException;
}