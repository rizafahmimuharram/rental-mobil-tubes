package com.mycompany.rental_mobil_project;

public interface PembayaranService {
    boolean prosesPembayaran(double total);
    String generateInvoice();
}
