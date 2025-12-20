package com.rental.model.user;

import com.rental.model.master.Mobil;
import com.rental.model.transaksi.Booking;


public class Admin extends User {
   
    public Admin(int id, String nama, String email, String password) {
        super(id, nama, email, password); 
    }
    
    public Admin(int id, String nama) {
       super(id, nama);
    }

    @Override
    public boolean login() {
        System.out.println("Admin " + this.getNama() + " masuk ke sistem manajemen.");
        return true; 
    }

    @Override
    public void logout() {
        System.out.println("Admin " + this.getNama() + " keluar dari sistem.");
    }

     
    public void kelolaMobil(Mobil m) {
        System.out.println("Admin " + this.getNama() + " sedang mengelola data Mobil: " + m.getNomorPlat());
        
    }
 
    public void kelolaPetugas(Petugas p) {
        System.out.println("Admin " + this.getNama() + " sedang mengelola data Petugas: " + p.getNama());
        
    }

   
    public void approveBooking(Booking b) {
        if (b.getStatus().equalsIgnoreCase("Pending")) {
            b.setStatus("Approved");
            
            b.getMobil().updateStatus("Rented"); 
            System.out.println("Admin " + this.getNama() + " menyetujui Booking ID " + b.getIdBooking() + ". Status mobil diubah menjadi 'Rented'.");
        } else {
            System.out.println("Booking ID " + b.getIdBooking() + " tidak dapat disetujui karena statusnya bukan 'Pending'.");
        }
    }
    
    public void rejectBooking(Booking b) {
        if (b.getStatus().equalsIgnoreCase("Pending")) {
            b.setStatus("Rejected");
            System.out.println("Admin " + this.getNama() + " menolak Booking ID " + b.getIdBooking() + ".");
        } else {
            System.out.println("Booking ID " + b.getIdBooking() + " tidak dapat ditolak karena statusnya bukan 'Pending'.");
        }
    }
    
    @Override
    public void tampilkanInfo() {
    super.tampilkanInfo(); // Menjalankan ID & Nama
    System.out.println("Status: Administrator System (Full Access)");
}
}