package com.mycompany.rental_mobil_project;

public class User {
    protected int id;
    protected String nama;
    protected String email;
    protected String password;

    public User(int id, String nama, String email, String password) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.password = password;
    }

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    public void logout() {
        System.out.println("Logout berhasil");
    }

    public void tampilkanInfo() {
        System.out.println("ID: " + id);
        System.out.println("Nama: " + nama);
        System.out.println("Email: " + email);
    }

    // getters (dipakai Main)
    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
}
