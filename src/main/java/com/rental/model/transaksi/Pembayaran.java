package com.rental.model.transaksi;

import com.rental.service.PembayaranService;
import java.time.LocalDateTime;

public class Pembayaran implements PembayaranService {
    
  
    private int idPembayaran;
    private LocalDateTime tanggalPembayaran;
    private String metode; 
    private double jumlahBayar;
    private String statusPembayaran; 

 
    public Pembayaran(int idPembayaran, LocalDateTime tanggalPembayaran, String metode, double jumlahBayar, String statusPembayaran) {
        this.idPembayaran = idPembayaran;
        this.tanggalPembayaran = tanggalPembayaran;
        this.metode = metode;
        this.jumlahBayar = jumlahBayar;
        this.statusPembayaran = statusPembayaran;
    }
    

    
    @Override
    public boolean prosesPembayaran(double total) {
        
        if (this.jumlahBayar >= total) {
            this.statusPembayaran = "Success";
            System.out.println("Pembayaran ID " + this.idPembayaran + " berhasil diproses sebesar " + this.jumlahBayar);
            return true;
        } else {
            this.statusPembayaran = "Failed";
            System.out.println("Pembayaran ID " + this.idPembayaran + " gagal: Jumlah kurang.");
            return false;
        }
    }
    
    @Override
    public String generateInvoice() {
        String invoice = "--- INVOICE ---"
                + "\nID Pembayaran: " + this.idPembayaran
                + "\nStatus: " + this.statusPembayaran
                + "\nJumlah: " + this.jumlahBayar
                + "\nTanggal: " + this.tanggalPembayaran.toLocalDate();
        
        return invoice;
    }

   

    public int getIdPembayaran() {
        return idPembayaran;
    }

    public LocalDateTime getTanggalPembayaran() {
        return tanggalPembayaran;
    }

    public String getMetode() {
        return metode;
    }

    public double getJumlahBayar() {
        return jumlahBayar;
    }

    public String getStatusPembayaran() {
        return statusPembayaran;
    }

    // Setter untuk update status di DAO
    public void setStatusPembayaran(String statusPembayaran) {
        this.statusPembayaran = statusPembayaran;
    }
   
    public void setIdPembayaran(int idPembayaran) {
    this.idPembayaran = idPembayaran;
    }
}