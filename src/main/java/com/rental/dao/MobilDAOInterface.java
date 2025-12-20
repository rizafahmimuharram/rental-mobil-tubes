package com.rental.dao;

import com.rental.model.master.Mobil;
import java.util.List;
import java.sql.SQLException;

public interface MobilDAOInterface {
    
    // Metode CRUD
    public void save(Mobil mobil) throws SQLException;
    public Mobil findById(int id) throws SQLException;
    public List<Mobil> findAll() throws SQLException;
    public void update(Mobil mobil) throws SQLException;
    public void delete(int id) throws SQLException;
    void updateStatus(int idMobil, String statusBaru) throws SQLException;
    
    // Metode Khusus (Custom Query)
    public List<Mobil> findAvailableMobil() throws SQLException;
}