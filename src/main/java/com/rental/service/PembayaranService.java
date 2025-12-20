package com.rental.service;


public interface PembayaranService {
    
  
    public boolean prosesPembayaran(double total);
    
    
    public String generateInvoice();
}