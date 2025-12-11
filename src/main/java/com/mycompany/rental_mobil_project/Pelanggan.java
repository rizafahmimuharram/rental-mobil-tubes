package com.mycompany.rental_mobil_project;

import java.util.ArrayList;
import java.util.List;

public class Pelanggan extends User {

    // atribut sesuai diagram (menggunakan nama yang ada di diagram sebelumnya)
    private String alamat;
    private String no_wa;
    private String no_ktp;
    private List<Booking> riwayatTransaksi;

    public Pelanggan(int id, String nama, String email, String password,
                     String alamat, String no_wa, String no_ktp) {
        super(id, nama, email, password);
        this.alamat = alamat;
        this.no_wa = no_wa;
        this.no_ktp = no_ktp;
        this.riwayatTransaksi = new ArrayList<>();
    }

    public void register() {
        System.out.println("Pelanggan terdaftar: " + nama);
    }

    // sesuai diagram: bookingMobil() mengembalikan Booking
    public Booking bookingMobil(Booking booking) {
        riwayatTransaksi.add(booking);
        return booking;
    }

    public List<Booking> lihatRiwayatTransaksi() {
        return riwayatTransaksi;
    }

    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.println("Alamat: " + alamat);
        System.out.println("No WA: " + no_wa);
        System.out.println("No KTP: " + no_ktp);
    }

    // getters
    public String getNo_wa() { return no_wa; }
    public String getNo_ktp() { return no_ktp; }
    public String getAlamat() { return alamat; }
}
