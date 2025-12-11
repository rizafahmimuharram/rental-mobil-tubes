package com.mycompany.rental_mobil_project;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Booking {

    private int idBooking;
    private LocalDate tanggalBooking;
    private LocalDate tanggalMulai;
    private LocalDate tanggalSelesai;
    private String status; // PENDING, APPROVED, REJECTED, BATAL, COMPLETED
    private double totalBiaya;
    private LocalDateTime waktuBatasBayar;
    private Pembayaran pembayaran;
    private Pengembalian pengembalian;
    private Mobil mobil;
    private Pelanggan pelanggan;

    public Booking(int idBooking, LocalDate tanggalMulai, LocalDate tanggalSelesai,
                   Mobil mobil, Pelanggan pelanggan) {
        this.idBooking = idBooking;
        this.tanggalBooking = LocalDate.now();
        this.tanggalMulai = tanggalMulai;
        this.tanggalSelesai = tanggalSelesai;
        this.mobil = mobil;
        this.pelanggan = pelanggan;
        this.status = "PENDING";
        this.totalBiaya = 0.0;
        this.waktuBatasBayar = LocalDateTime.now().plusHours(24); // default
    }

    public double hitungTotalBiaya() {
        long hari = ChronoUnit.DAYS.between(tanggalMulai, tanggalSelesai);
        if (hari <= 0) hari = 1;
        totalBiaya = mobil.hitungBiaya((int) hari);
        return totalBiaya;
    }

    public void batalBooking() {
        this.status = "BATAL";
    }

    public boolean cekStatusMobil() {
        return mobil.cekKetersediaan();
    }

    public double hitungDendaSewa() {
        // sederhana: denda 10% dari total jika terlambat (placeholder)
        return totalBiaya * 0.1;
    }

    public boolean prosesPembayaran(Pembayaran pembayaran) {
        this.pembayaran = pembayaran;
        boolean ok = pembayaran.prosesPembayaran(totalBiaya);
        if (ok) {
            this.status = "PAID";
            mobil.updateStatus("rented");
        }
        return ok;
    }

    public boolean cekKedaluwarsa() {
        return LocalDateTime.now().isAfter(waktuBatasBayar);
    }

    // setters and getters required oleh Main
    public void setPengembalian(Pengembalian pengembalian) { this.pengembalian = pengembalian; }
    public Pengembalian getPengembalian() { return pengembalian; }

    public int getIdBooking() { return idBooking; }
    public LocalDate getTanggalMulai() { return tanggalMulai; }
    public LocalDate getTanggalSelesai() { return tanggalSelesai; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getTotalBiaya() { return totalBiaya; }
    public Pembayaran getPembayaran() { return pembayaran; }
    public Mobil getMobil() { return mobil; }
    public Pelanggan getPelanggan() { return pelanggan; }
    public LocalDate getTanggalBooking() { return tanggalBooking; }
}
