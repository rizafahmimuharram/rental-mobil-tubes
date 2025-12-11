package com.mycompany.rental_mobil_project;

public class Admin extends User {

    public Admin(int id, String nama, String email, String password) {
        super(id, nama, email, password);
    }

    // method sesuai diagram (kosongkan body sesuai instruksi; implementasi minimal agar dapat dipanggil)
    public void kelolaMobil(Mobil mobil) {
        // implementasi di Main (admin menu) akan manipulasi daftar mobil
    }

    public void kelolaPetugas() {
        // reserved
    }

    public void approveBooking(Booking booking) {
        booking.setStatus("APPROVED");
        System.out.println("Booking " + booking.getIdBooking() + " diset APPROVED");
    }

    public void rejectBooking(Booking booking) {
        booking.setStatus("REJECTED");
        System.out.println("Booking " + booking.getIdBooking() + " diset REJECTED");
    }

    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.println("Role: Admin");
    }
}
