package com.rental.model.user;

import com.rental.model.transaksi.Booking;
import java.util.ArrayList;
import java.util.List;

public class Pelanggan extends User {
    private String alamat;
    private String no_wa; 
    private String no_ktp; 
    private List<Booking> riwayatTransaksi; 

    public Pelanggan(int id, String nama, String email, String password, String no_wa, String alamat, String no_ktp) {
        super(id, nama, email, password); 
        this.no_wa = no_wa;
        this.alamat = alamat;
        this.no_ktp = no_ktp;
        this.riwayatTransaksi = new ArrayList<>(); 
    }
    
    public Pelanggan(int id, String nama) {
        super(id, nama);
    }
    
    @Override
    public boolean login() {
        System.out.println("Pelanggan " + this.getNama() + " berhasil login.");
        return true; 
    }

    @Override
    public void logout() {
        System.out.println("Pelanggan " + this.getNama() + " telah logout.");
    }

    @Override
    public void tampilkanInfo() {
       super.tampilkanInfo(); // Memanggil ID & Nama dari parent
        System.out.println("Alamat: " + alamat);
        System.out.println("WhatsApp: " + no_wa);
        System.out.println("KTP: " + no_ktp);
    }
    
  
    public void register() {
        System.out.println("Pendaftaran pelanggan baru (" + this.getNama() + ") berhasil.");
    }
    
    
    public Booking bookingMobil(Booking bookingBaru) {
        this.riwayatTransaksi.add(bookingBaru); 
        System.out.println("Booking ID " + bookingBaru.getIdBooking() + " dibuat oleh " + this.getNama());
        return bookingBaru;
    }
    
    
    public List<Booking> lihatRiwayatTransaksi() {
        System.out.println("Menampilkan " + this.riwayatTransaksi.size() + " riwayat transaksi untuk " + this.getNama());
        return this.riwayatTransaksi;
    }


    
    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoWa() {
        return no_wa;
    }

    public void setNoWa(String no_wa) {
        this.no_wa = no_wa;
    }

    public String getNoKtp() {
        return no_ktp;
    }

    public void setNoKtp(String no_ktp) {
        this.no_ktp = no_ktp;
    }
    
    public List<Booking> getRiwayatTransaksi() {
        return riwayatTransaksi;
    }
}