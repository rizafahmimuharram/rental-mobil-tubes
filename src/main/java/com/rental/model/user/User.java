package com.rental.model.user;

public abstract class User {
    private int id;
    private String nama;
    private String email;
    private String password; 

    public User(int id, String nama, String email, String password) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.password = password;
    }
    
    public User(int id, String nama) {
        this.id = id;
        this.nama = nama;
    }
    
    public abstract boolean login();
    public abstract void logout();
    
    public void tampilkanInfo() {
        System.out.println("ID: " + id);
        System.out.println("Nama: " + nama);
        System.out.println("Role: " + this.getClass().getSimpleName());
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }
    
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    
    
    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setId(int id) {
        this.id = id;
    }
}