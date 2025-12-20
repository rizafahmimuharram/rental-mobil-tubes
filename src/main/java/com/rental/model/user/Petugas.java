package com.rental.model.user;

import com.rental.model.transaksi.Booking;
import com.rental.model.transaksi.Pengembalian;
import java.time.LocalDate;

public class Petugas extends User {
    private String no_telp;
    private String shift; 
    
    public Petugas(int id, String nama, String email, String password, String no_telp, String shift) {
        super(id, nama, email, password); 
        this.no_telp = no_telp;
        this.shift = shift;
    }
    
    public Petugas(int id, String nama, String shift) {
       super(id, nama);
    }

    @Override
    public boolean login() {
        System.out.println("Petugas " + this.getNama() + " berhasil login.");
        return true; 
    }

    @Override
    public void logout() {
        System.out.println("Petugas " + this.getNama() + " telah logout.");
    }

    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo(); 
        System.out.println("No. Telepon: " + no_telp);
        System.out.println("Shift Kerja: " + shift);
    }
    
    public void terimaMobil(Booking bookingTerkait, double denda, String catatan) {
        Pengembalian pengembalianBaru = new Pengembalian(
            0, 
            LocalDate.now(), 
            denda, 
            catatan
        );
        bookingTerkait.setPengembalian(pengembalianBaru);
        bookingTerkait.getMobil().updateStatus("Ready"); 
        bookingTerkait.setStatus("Completed");
        System.out.println("Pengembalian untuk Booking ID " + bookingTerkait.getIdBooking() + " berhasil diproses.");
    }

    
    public String getNoTelp() {
        return no_telp;
    }

    public void setNoTelp(String no_telp) {
        this.no_telp = no_telp;
    }
    
    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }
}