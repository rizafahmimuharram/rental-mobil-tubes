package com.mycompany.rental_mobil_project;

public class Mobil implements CekKetersediaanInterface {

    private int id;
    private int jumlahKursi;
    private String tipeMesin;
    private String merek;
    private String model;
    private String nomorPlat;
    private String status; // "available" / "rented" / etc
    private double hargaSewaPerHari;

    public Mobil(int id, int jumlahKursi, String tipeMesin, String merek, String model,
                 String nomorPlat, String status, double hargaSewaPerHari) {
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
        System.out.println("ID: " + id + " | " + merek + " " + model + " | Plat: " + nomorPlat + " | Status: " + status + " | Harga/Hari: " + hargaSewaPerHari);
    }

    @Override
    public boolean cekKetersediaan() {
        return status != null && status.equalsIgnoreCase("available");
    }

    public double hitungBiaya(int jumlahHari) {
        return jumlahHari * hargaSewaPerHari;
    }

    public void updateStatus(String statusBaru) {
        this.status = statusBaru;
    }

    // getters
    public int getId() { return id; }
    public String getMerek() { return merek; }
    public String getModel() { return model; }
    public String getNomorPlat() { return nomorPlat; }
    public String getStatus() { return status; }
    public double getHargaSewaPerHari() { return hargaSewaPerHari; }
}
