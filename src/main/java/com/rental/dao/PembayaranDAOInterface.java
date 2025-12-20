package com.rental.dao;

import com.rental.model.transaksi.Pembayaran;
import java.sql.SQLException;

public interface PembayaranDAOInterface {
    
    public void save(Pembayaran pembayaran, int bookingId) throws SQLException;
    public Pembayaran findByBookingId(int bookingId) throws SQLException;
    public void updateStatus(int idPembayaran, String statusBaru) throws SQLException;
}