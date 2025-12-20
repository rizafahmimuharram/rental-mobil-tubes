package com.rental.model.transaksi;

import java.time.LocalDate;

public class Pengembalian {
    
   
    private int idPengembalian;
    private LocalDate tglKembali; 
    private String catatan;     
    private double denda;      

  
    public Pengembalian(int idPengembalian, LocalDate tglKembali, double denda, String catatan) {
        this.idPengembalian = idPengembalian;
        this.tglKembali = tglKembali;
        this.denda = denda;
        this.catatan = catatan;
    }
    
   
    public int getIdPengembalian() {
        return idPengembalian;
    }

    public void setIdPengembalian(int idPengembalian) {
        this.idPengembalian = idPengembalian;
    }

    public LocalDate getTglKembali() {
        return tglKembali;
    }

    public void setTglKembali(LocalDate tglKembali) {
        this.tglKembali = tglKembali;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public double getDenda() {
        return denda;
    }

    public void setDenda(double denda) {
        this.denda = denda;
    }
}