package com.mycompany.rental_mobil_project;

public class Petugas extends User {

    private String shift;
    private String no_telp;

    public Petugas(int id, String nama, String email, String password, String shift, String no_telp) {
        super(id, nama, email, password);
        this.shift = shift;
        this.no_telp = no_telp;
    }

    public void terimaMobil(Booking booking) {
        // proses pengembalian di Main; di sini tampilkan placeholder
        System.out.println("Petugas menerima mobil untuk booking: " + booking.getIdBooking());
    }

    public void updateStatusMobil(Mobil mobil, String statusBaru) {
        mobil.updateStatus(statusBaru);
        System.out.println("Status mobil " + mobil.getNomorPlat() + " diupdate menjadi " + statusBaru);
    }

    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.println("Shift: " + shift);
        System.out.println("No Telp: " + no_telp);
    }
}
