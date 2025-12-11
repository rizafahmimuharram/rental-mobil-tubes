package com.mycompany.rental_mobil_project;

import java.time.LocalDateTime;

public class Pembayaran implements PembayaranService {

    private int id;
    private String metode;
    private double jumlahBayar;
    private LocalDateTime tanggalPembayaran;
    private String statusPembayaran;

    public Pembayaran(int id, LocalDateTime tanggalPembayaran, String metode,
                      double jumlah, String status) {
        this.id = id;
        this.tanggalPembayaran = tanggalPembayaran;
        this.metode = metode;
        this.jumlahBayar = jumlah;
        this.statusPembayaran = status;
    }

    @Override
    public boolean prosesPembayaran(double total) {
        boolean ok = jumlahBayar >= total;
        if (ok) statusPembayaran = "PAID"; else statusPembayaran = "FAILED";
        return ok;
    }

    @Override
    public String generateInvoice() {
        return "Invoice Pembayaran ID: " + id + " | Metode: " + metode + " | Jumlah: " + jumlahBayar;
    }

    // getters
    public int getId() { return id; }
    public String getMetode() { return metode; }
    public double getJumlahBayar() { return jumlahBayar; }
    public String getStatusPembayaran() { return statusPembayaran; }
}
