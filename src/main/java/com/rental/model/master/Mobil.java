package com.rental.model.master;

import com.rental.service.CekKetersediaan;

public class Mobil implements CekKetersediaan {
    private int id;
    private int jumlahKursi;
    private String tipeMesin; 
    private String merek;
    private String model;
    private String nomorPlat;
    private String status; 
    private double hargaSewaPerHari;
    
    public Mobil(int id, int jumlahKursi, String tipeMesin, String merek, String model, String nomorPlat, String status, double hargaSewaPerHari) {
        this.id = id;
        this.jumlahKursi = jumlahKursi;
        this.tipeMesin = tipeMesin;
        this.merek = merek;
        this.model = model;
        this.nomorPlat = nomorPlat;
        this.status = status;
        this.hargaSewaPerHari = hargaSewaPerHari;
    }
    
    public void tampilkanInfo() {
        System.out.println(" DATA MOBIL ");
        System.out.println("ID: " + this.id);
        System.out.println("Merek/Model: " + this.merek + " " + this.model);
        System.out.println("Plat: " + this.nomorPlat);
        System.out.println("Status: " + this.status);
        System.out.println("Harga Sewa: " + this.hargaSewaPerHari);
    }
    
    public double hitungBiaya(int jumlahHari) {
        if (jumlahHari <= 0) {
            return 0;
        }
        return this.hargaSewaPerHari * jumlahHari;
    }
    
    public void updateStatus(String statusBaru) {
        this.status = statusBaru;
        System.out.println("Status Mobil " + this.nomorPlat + " diubah menjadi: " + statusBaru);
    }

   
    
   @Override
    public boolean cekKetersediaan() {
        return this.status.equalsIgnoreCase("Ready");
    }

 
    public int getId() {
        return id;
    }

    public int getJumlahKursi() {
        return jumlahKursi;
    }

    public String getTipeMesin() {
        return tipeMesin;
    }

    public String getMerek() {
        return merek;
    }

    public String getModel() {
        return model;
    }

    public String getNomorPlat() {
        return nomorPlat;
    }

    public String getStatus() {
        return status;
    }

    public double getHargaSewaPerHari() {
        return hargaSewaPerHari;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setJumlahKursi(int jumlahKursi) {
        this.jumlahKursi = jumlahKursi;
    }

    public void setTipeMesin(String tipeMesin) {
        this.tipeMesin = tipeMesin;
    }

    public void setMerek(String merek) {
        this.merek = merek;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setNomorPlat(String nomorPlat) {
        this.nomorPlat = nomorPlat;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setHargaSewaPerHari(double hargaSewaPerHari) {
        this.hargaSewaPerHari = hargaSewaPerHari;
    }
}