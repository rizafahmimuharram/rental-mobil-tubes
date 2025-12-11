package com.mycompany.rental_mobil_project;

import java.time.LocalDate;

public class Pengembalian {

    private int idPengembalian;
    private LocalDate tglKembali;
    private String catatan;
    private double denda;

    public Pengembalian(int idPengembalian, LocalDate tanggalKembali, double denda, String catatan) {
        this.idPengembalian = idPengembalian;
        this.tglKembali = tanggalKembali;
        this.denda = denda;
        this.catatan = catatan;
    }

    public void prosesPengembalian() {
        System.out.println("Pengembalian diproses. ID Pengembalian: " + idPengembalian);
    }

    // getters
    public int getIdPengembalian() { return idPengembalian; }
    public LocalDate getTglKembali() { return tglKembali; }
    public String getCatatan() { return catatan; }
    public double getDenda() { return denda; }
}
