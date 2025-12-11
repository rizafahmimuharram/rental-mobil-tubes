package com.mycompany.rental_mobil_project;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in);

    // "database" sementara
    private static List<Pelanggan> pelangganList = new ArrayList<>();
    private static List<Petugas> petugasList = new ArrayList<>();
    private static List<Admin> adminList = new ArrayList<>();
    private static List<Mobil> mobilList = new ArrayList<>();
    private static List<Booking> bookingList = new ArrayList<>();
    private static List<Pembayaran> pembayaranList = new ArrayList<>();
    private static List<Pengembalian> pengembalianList = new ArrayList<>();

    private static int nextBookingId = 1;
    private static int nextPembayaranId = 1;
    private static int nextPengembalianId = 1;

    public static void main(String[] args) {
        seedData();
        menuUtama();
    }

    private static void seedData() {
        adminList.add(new Admin(1, "admin", "admin@mail.com", "admin"));
        petugasList.add(new Petugas(1, "petugas", "petugas@mail.com", "petugas", "Pagi", "081234"));
        pelangganList.add(new Pelanggan(1, "budi", "budi@mail.com", "123", "Jl. A", "081100", "98765"));

        mobilList.add(new Mobil(1, 5, "Bensin", "Toyota", "Avanza", "B1234AA", "available", 300000));
        mobilList.add(new Mobil(2, 5, "Bensin", "Honda", "Brio", "B2222BB", "available", 250000));
    }

    private static void menuUtama() {
        while (true) {
            System.out.println("\n=== SISTEM RENTAL MOBIL ===");
            System.out.println("1. Login Admin");
            System.out.println("2. Login Pelanggan");
            System.out.println("3. Login Petugas");
            System.out.println("4. Register Pelanggan");
            System.out.println("5. Exit");
            System.out.print("Pilih: ");
            int pilih = Integer.parseInt(sc.nextLine().trim());
            switch (pilih) {
                case 1 -> loginAdmin();
                case 2 -> loginPelanggan();
                case 3 -> loginPetugas();
                case 4 -> registerPelanggan();
                case 5 -> {
                    System.out.println("Keluar. Bye!");
                    return;
                }
                default -> System.out.println("Pilihan tidak valid.");
            }
        }
    }

    // ------------------ AUTH -----------------------
    private static Admin authAdmin(String email, String pass) {
        return adminList.stream().filter(a -> a.getEmail().equals(email) && a.password.equals(pass)).findFirst().orElse(null);
    }

    private static Pelanggan authPelanggan(String email, String pass) {
        return pelangganList.stream().filter(p -> p.getEmail().equals(email) && p.password.equals(pass)).findFirst().orElse(null);
    }

    private static Petugas authPetugas(String email, String pass) {
        return petugasList.stream().filter(p -> p.getEmail().equals(email) && p.password.equals(pass)).findFirst().orElse(null);
    }

    // ------------------ MENU LOGIN -----------------------
    private static void loginAdmin() {
        System.out.print("Email: ");
        String e = sc.nextLine().trim();
        System.out.print("Password: ");
        String p = sc.nextLine().trim();
        Admin admin = authAdmin(e, p);
        if (admin != null) menuAdmin(admin);
        else System.out.println("Login gagal!");
    }

    private static void loginPelanggan() {
        System.out.print("Email: ");
        String e = sc.nextLine().trim();
        System.out.print("Password: ");
        String p = sc.nextLine().trim();
        Pelanggan pel = authPelanggan(e, p);
        if (pel != null) menuPelanggan(pel);
        else System.out.println("Login gagal!");
    }

    private static void loginPetugas() {
        System.out.print("Email: ");
        String e = sc.nextLine().trim();
        System.out.print("Password: ");
        String p = sc.nextLine().trim();
        Petugas pet = authPetugas(e, p);
        if (pet != null) menuPetugas(pet);
        else System.out.println("Login gagal!");
    }

    // ------------------ REGISTER -----------------------
    private static void registerPelanggan() {
        System.out.print("ID: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Nama: ");
        String nama = sc.nextLine().trim();
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Password: ");
        String pass = sc.nextLine().trim();
        System.out.print("Alamat: ");
        String alamat = sc.nextLine().trim();
        System.out.print("No WA: ");
        String no_wa = sc.nextLine().trim();
        System.out.print("No KTP: ");
        String no_ktp = sc.nextLine().trim();

        Pelanggan p = new Pelanggan(id, nama, email, pass, alamat, no_wa, no_ktp);
        pelangganList.add(p);
        System.out.println("Registrasi berhasil.");
    }

    // ------------------ MENU ADMIN -----------------------
    private static void menuAdmin(Admin admin) {
        while (true) {
            System.out.println("\n--- MENU ADMIN ---");
            System.out.println("1. Lihat Daftar Mobil");
            System.out.println("2. Tambah Mobil");
            System.out.println("3. Update Status Mobil");
            System.out.println("4. Lihat Booking");
            System.out.println("5. Logout");
            System.out.print("Pilih: ");
            int pil = Integer.parseInt(sc.nextLine().trim());
            switch (pil) {
                case 1 -> listMobil();
                case 2 -> tambahMobil();
                case 3 -> updateStatusMobilAdmin();
                case 4 -> lihatSemuaBooking();
                case 5 -> {
                    System.out.println("Logout admin.");
                    return;
                }
                default -> System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void listMobil() {
        System.out.println("\n== DAFTAR MOBIL ==");
        for (Mobil m : mobilList) {
            m.tampilkanInfo();
        }
    }

    private static void tambahMobil() {
        System.out.print("ID Mobil: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Jumlah Kursi: ");
        int kursi = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Tipe Mesin: ");
        String tipe = sc.nextLine().trim();
        System.out.print("Merek: ");
        String merek = sc.nextLine().trim();
        System.out.print("Model: ");
        String model = sc.nextLine().trim();
        System.out.print("Nomor Plat: ");
        String plat = sc.nextLine().trim();
        System.out.print("Harga Sewa/Hari: ");
        double harga = Double.parseDouble(sc.nextLine().trim());

        mobilList.add(new Mobil(id, kursi, tipe, merek, model, plat, "available", harga));
        System.out.println("Mobil berhasil ditambahkan.");
    }

    private static void updateStatusMobilAdmin() {
        System.out.print("Masukkan nomor plat mobil: ");
        String plat = sc.nextLine().trim();
        Mobil target = findMobilByPlat(plat);
        if (target == null) {
            System.out.println("Mobil tidak ditemukan.");
            return;
        }
        System.out.print("Status baru (available/rented/maintenance): ");
        String status = sc.nextLine().trim();
        target.updateStatus(status);
        System.out.println("Status terupdate.");
    }

    private static void lihatSemuaBooking() {
        System.out.println("\n== DAFTAR BOOKING ==");
        for (Booking b : bookingList) {
            System.out.println("Booking ID: " + b.getIdBooking() + " | Pelanggan: " + b.getPelanggan().getNama() +
                    " | Mobil: " + b.getMobil().getMerek() + " " + b.getMobil().getModel() +
                    " | Status: " + b.getStatus() + " | Total: " + b.getTotalBiaya());
        }
    }

    // ------------------ MENU PELANGGAN -----------------------
    private static void menuPelanggan(Pelanggan pel) {
        while (true) {
            System.out.println("\n--- MENU PELANGGAN (" + pel.getNama() + ") ---");
            System.out.println("1. Lihat Daftar Mobil");
            System.out.println("2. Booking Mobil");
            System.out.println("3. Lihat Riwayat Transaksi");
            System.out.println("4. Bayar Booking");
            System.out.println("5. Batal Booking");
            System.out.println("6. Logout");
            System.out.print("Pilih: ");
            int pil = Integer.parseInt(sc.nextLine().trim());
            switch (pil) {
                case 1 -> listMobil();
                case 2 -> lakukanBooking(pel);
                case 3 -> lihatRiwayat(pel);
                case 4 -> bayarBooking(pel);
                case 5 -> batalBookingPelanggan(pel);
                case 6 -> {
                    System.out.println("Logout pelanggan.");
                    return;
                }
                default -> System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void lakukanBooking(Pelanggan pel) {
        System.out.print("Masukkan nomor plat mobil yang ingin dibooking: ");
        String plat = sc.nextLine().trim();
        Mobil mobil = findMobilByPlat(plat);
        if (mobil == null) {
            System.out.println("Mobil tidak ditemukan.");
            return;
        }
        if (!mobil.cekKetersediaan()) {
            System.out.println("Mobil tidak tersedia.");
            return;
        }
        System.out.print("Tanggal mulai (YYYY-MM-DD): ");
        LocalDate mulai = LocalDate.parse(sc.nextLine().trim());
        System.out.print("Tanggal selesai (YYYY-MM-DD): ");
        LocalDate selesai = LocalDate.parse(sc.nextLine().trim());

        Booking booking = new Booking(nextBookingId++, mulai, selesai, mobil, pel);
        booking.hitungTotalBiaya();
        bookingList.add(booking);
        pel.bookingMobil(booking); // simpan di riwayat pelanggan

        System.out.println("Booking dibuat. ID Booking: " + booking.getIdBooking() +
                " | Total Biaya: " + booking.getTotalBiaya() +
                " | Status: " + booking.getStatus());
    }

    private static void lihatRiwayat(Pelanggan pel) {
        System.out.println("\n== RIWAYAT TRANSAKSI " + pel.getNama() + " ==");
        for (Booking b : pel.lihatRiwayatTransaksi()) {
            System.out.println("Booking ID: " + b.getIdBooking() + " | Mobil: " + b.getMobil().getMerek() + " " + b.getMobil().getModel()
                    + " | Tgl Mulai: " + b.getTanggalMulai() + " | Tgl Selesai: " + b.getTanggalSelesai() + " | Status: " + b.getStatus()
                    + " | Total: " + b.getTotalBiaya());
        }
    }

    private static void bayarBooking(Pelanggan pel) {
        System.out.print("Masukkan ID Booking yang ingin dibayar: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        Booking booking = findBookingByIdAndPelanggan(id, pel);
        if (booking == null) {
            System.out.println("Booking tidak ditemukan.");
            return;
        }
        if (booking.getPembayaran() != null) {
            System.out.println("Booking sudah dibayar.");
            return;
        }
        System.out.println("Total yang harus dibayar: " + booking.getTotalBiaya());
        System.out.print("Masukkan jumlah bayar: ");
        double jumlah = Double.parseDouble(sc.nextLine().trim());
        System.out.print("Metode pembayaran (cash/transfer): ");
        String metode = sc.nextLine().trim();

        Pembayaran pay = new Pembayaran(nextPembayaranId++, LocalDateTime.now(), metode, jumlah, "PENDING");
        boolean ok = booking.prosesPembayaran(pay);
        pembayaranList.add(pay);

        if (ok) {
            System.out.println("Pembayaran berhasil. Invoice: " + pay.generateInvoice());
        } else {
            System.out.println("Pembayaran gagal. Jumlah kurang dari total.");
        }
    }

    private static void batalBookingPelanggan(Pelanggan pel) {
        System.out.print("Masukkan ID Booking yang ingin dibatalkan: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        Booking booking = findBookingByIdAndPelanggan(id, pel);
        if (booking == null) {
            System.out.println("Booking tidak ditemukan.");
            return;
        }
        booking.batalBooking();
        System.out.println("Booking dibatalkan.");
    }

    // ------------------ MENU PETUGAS -----------------------
    private static void menuPetugas(Petugas pet) {
        while (true) {
            System.out.println("\n--- MENU PETUGAS (" + pet.getNama() + ") ---");
            System.out.println("1. Lihat Booking Pending/Approved");
            System.out.println("2. Terima Pengembalian Mobil");
            System.out.println("3. Update Status Mobil");
            System.out.println("4. Logout");
            System.out.print("Pilih: ");
            int pil = Integer.parseInt(sc.nextLine().trim());
            switch (pil) {
                case 1 -> lihatBookingUntukPetugas();
                case 2 -> terimaPengembalian(pet);
                case 3 -> updateStatusMobilPetugas(pet);
                case 4 -> { System.out.println("Logout petugas."); return; }
                default -> System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void lihatBookingUntukPetugas() {
        System.out.println("\n== Booking == ");
        for (Booking b : bookingList) {
            System.out.println("ID: " + b.getIdBooking() + " | Pel: " + b.getPelanggan().getNama()
                    + " | Mobil: " + b.getMobil().getMerek() + " " + b.getMobil().getModel()
                    + " | Status: " + b.getStatus());
        }
    }

    private static void terimaPengembalian(Petugas pet) {
        System.out.print("Masukkan ID Booking untuk proses pengembalian: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        Booking booking = findBookingById(id);
        if (booking == null) {
            System.out.println("Booking tidak ditemukan.");
            return;
        }
        // buat record pengembalian
        System.out.print("Catatan kondisi mobil: ");
        String catatan = sc.nextLine().trim();
        double denda = 0.0;
        // hitung denda jika ada (sederhana: jika hari kembali > tanggalSelesai)
        LocalDate expected = booking.getTanggalSelesai();
        LocalDate actual = LocalDate.now();
        if (actual.isAfter(expected)) {
            denda = booking.hitungDendaSewa();
        }
        Pengembalian peng = new Pengembalian(nextPengembalianId++, actual, denda, catatan);
        pengembalianList.add(peng);
        booking.setPengembalian(peng);
        booking.setStatus("COMPLETED");
        booking.getMobil().updateStatus("available");
        pet.terimaMobil(booking);
        System.out.println("Pengembalian diproses. Denda: " + denda);
    }

    private static void updateStatusMobilPetugas(Petugas pet) {
        System.out.print("Masukkan nomor plat mobil: ");
        String plat = sc.nextLine().trim();
        Mobil m = findMobilByPlat(plat);
        if (m == null) {
            System.out.println("Mobil tidak ditemukan.");
            return;
        }
        System.out.print("Masukkan status baru: ");
        String status = sc.nextLine().trim();
        pet.updateStatusMobil(m, status);
    }

    // ------------------ HELPER -----------------------
    private static Mobil findMobilByPlat(String plat) {
        return mobilList.stream().filter(m -> m.getNomorPlat().equalsIgnoreCase(plat)).findFirst().orElse(null);
    }

    private static Booking findBookingById(int id) {
        return bookingList.stream().filter(b -> b.getIdBooking() == id).findFirst().orElse(null);
    }

    private static Booking findBookingByIdAndPelanggan(int id, Pelanggan pel) {
        return bookingList.stream().filter(b -> b.getIdBooking() == id && b.getPelanggan().equals(pel)).findFirst().orElse(null);
    }
}
