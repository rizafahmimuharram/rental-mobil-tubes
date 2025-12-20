package com.rental.model.transaksi;

import com.rental.model.user.Pelanggan;
import com.rental.model.master.Mobil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Booking {
    
   
    private int idBooking;
    private LocalDate tanggalBooking;
    private LocalDate tanggalMulai;
    private LocalDate tanggalSelesai;
    private String status; 
    private double totalBiaya;
    private LocalDateTime waktuBatasBayar; 


    private Mobil mobil;         
    private Pelanggan pelanggan; 
    private Pembayaran pembayaran;
    private Pengembalian pengembalian; 

    
    public Booking(int idBooking, LocalDate tanggalMulai, LocalDate tanggalSelesai, Mobil mobil, Pelanggan pelanggan) {
        this.idBooking = idBooking;
        this.tanggalMulai = tanggalMulai;
        this.tanggalSelesai = tanggalSelesai;
        this.mobil = mobil;
        this.pelanggan = pelanggan;
        this.tanggalBooking = LocalDate.now();
        this.status = "Pending";
        this.waktuBatasBayar = LocalDateTime.now().plusHours(12); 
        this.totalBiaya = hitungTotalBiaya();
        this.pembayaran = null;
        this.pengembalian = null;
    }
    

    public int hitungDurasiSewa() {

        long days = ChronoUnit.DAYS.between(tanggalMulai, tanggalSelesai);
        
        return (int) days + 1;
    }

 
    public double hitungTotalBiaya() {
        int durasi = hitungDurasiSewa();
        // Delegasi perhitungan ke objek Mobil
        return this.mobil.hitungBiaya(durasi);
    }
    
    public boolean cekStatusMobil() {
        return this.mobil.cekKetersediaan();
    }
    
    
    public void batalBooking() {
        if (this.status.equalsIgnoreCase("Pending") || this.status.equalsIgnoreCase("Approved")) {
            this.status = "Canceled";
            
            if (this.mobil != null) {
                this.mobil.updateStatus("Ready");
            }
            System.out.println("Booking ID " + this.idBooking + " berhasil dibatalkan.");
        } else {
            System.out.println("Booking tidak dapat dibatalkan karena sudah " + this.status);
        }
    }

  
    public boolean cekKedaluwarsa() {
        return LocalDateTime.now().isAfter(this.waktuBatasBayar) && this.status.equalsIgnoreCase("Pending");
    }
    
 
    public boolean prosesPembayaran() {
        if (this.pembayaran != null) {
            
            return this.pembayaran.prosesPembayaran(this.totalBiaya);
        }
        System.out.println("Objek Pembayaran belum diset pada Booking ID " + this.idBooking);
        return false;
    }
    
    
   
    public void setPembayaran(Pembayaran pembayaran) {
        this.pembayaran = pembayaran;
    }

  
    public void setPengembalian(Pengembalian pengembalian) {
        this.pengembalian = pengembalian;
    }
    
    
    public int getIdBooking() {
        return idBooking;
    }

    public LocalDate getTanggalMulai() {
        return tanggalMulai;
    }

    public LocalDate getTanggalSelesai() {
        return tanggalSelesai;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalBiaya() {
        return totalBiaya;
    }

    public Mobil getMobil() {
        return mobil;
    }

    public Pelanggan getPelanggan() {
        return pelanggan;
    }
    
  public void setIdBooking(int idBooking) {
      this.idBooking = idBooking;
  }
  
  public void setTotalBiaya(double totalBiaya) {
      this.totalBiaya = totalBiaya;
  }
  
  public LocalDate getTanggalBooking(){
      return this.tanggalBooking;
  }
  
  public LocalDateTime getWaktuBatasBayar() {
  return this.waktuBatasBayar;
  }
}
