package com.rental.dao;

import com.rental.model.transaksi.Booking;
import java.sql.SQLException;
import java.util.List;

public interface BookingDAOInterface {
    
    
    public void save(Booking booking) throws SQLException;
    public Booking findById(int id) throws SQLException;
    public List<Booking> findAll() throws SQLException;
    public void update(Booking booking) throws SQLException;
    public void delete(int id) throws SQLException;
    
   
    List<Booking> findActiveBookings() throws SQLException;
    public List<Booking> findPendingBookings() throws SQLException;
    public void updateStatus(int bookingId, String statusBaru) throws SQLException;
    List<Booking> findBookingsByPelanggan(int pelangganId) throws SQLException;
    
    public List<Booking> findByStatus(String status) throws SQLException;
   
}