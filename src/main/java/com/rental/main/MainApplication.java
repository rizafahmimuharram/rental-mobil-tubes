package com.rental.main;

import com.rental.dao.UserDAO;
import com.rental.dao.BookingDAO;
import com.rental.dao.MobilDAO;
import com.rental.model.master.Mobil;
import com.rental.model.transaksi.Booking; // Import class Booking
import com.rental.model.user.Admin;
import com.rental.model.user.Pelanggan;
import com.rental.model.user.Petugas;
import com.rental.model.user.User;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MainApplication {
    private static final UserDAO userDAO = new UserDAO();
    private static final MobilDAO mobilDAO = new MobilDAO();
    private static final BookingDAO bookingDAO = new BookingDAO();
    private static final Scanner scanner = new Scanner(System.in);
    private static User userLogged = null;

    public static void main(String[] args) {
        while (true) {
            tampilkanMainMenu();
        }
    }

    private static void tampilkanMainMenu() {
        System.out.println("\n========================================");
        System.out.println("      SISTEM RENTAL MOBIL CONSOLE        ");
        System.out.println("========================================");
        System.out.println("1. Login");
        System.out.println("2. Registrasi Pelanggan Baru");
        System.out.println("3. Keluar");
        System.out.print("Pilih Menu: ");
        
        String pilihan = scanner.nextLine();
        switch (pilihan) {
            case "1": prosesLogin(); break;
            case "2": prosesRegistrasi(); break;
            case "3": 
                System.out.println("Terima kasih telah menggunakan layanan kami.");
                System.exit(0);
            default: 
                System.out.println("Pilihan tidak valid!");
        }
    }

    private static void prosesLogin() {
        System.out.println("\n--- FORM LOGIN ---");
        System.out.print("Email   : "); String email = scanner.nextLine();
        System.out.print("Password: "); String pass = scanner.nextLine();

        try {
            userLogged = userDAO.login(email, pass);
            if (userLogged != null) {
                System.out.println("\n[SUCCESS] Login Berhasil!");
                userLogged.login(); 
                
                if (userLogged instanceof Admin) dashboardAdmin();
                else if (userLogged instanceof Petugas) dashboardPetugas();
                else if (userLogged instanceof Pelanggan) dashboardPelanggan();
                
            } else {
                System.out.println("[FAILED] Email atau Password salah!");
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
        }
    }

    private static void prosesRegistrasi() {
        System.out.println("\n--- FORM REGISTRASI PELANGGAN ---");
        System.out.print("Nama Lengkap : "); String nama = scanner.nextLine();
        System.out.print("Email        : "); String email = scanner.nextLine();
        System.out.print("Password     : "); String pass = scanner.nextLine();
        System.out.print("Alamat       : "); String alamat = scanner.nextLine();
        System.out.print("No WhatsApp  : "); String wa = scanner.nextLine();
        System.out.print("No KTP       : "); String ktp = scanner.nextLine();

        Pelanggan p = new Pelanggan(0, nama, email, pass, alamat, wa, ktp);
        try {
            userDAO.save(p);
            System.out.println("[SUCCESS] Akun berhasil dibuat! Silakan login.");
        } catch (SQLException e) {
            System.err.println("[ERROR] Gagal registrasi: " + e.getMessage());
        }
    }

    // =========================================================================
    // DASHBOARD ADMIN
    // =========================================================================
    private static void dashboardAdmin() {
        boolean logout = false;
        while (!logout) {
            System.out.println("\n=== DASHBOARD ADMIN: " + userLogged.getNama() + " ===");
            System.out.println("1. Kelola Data Mobil (Master)");
            System.out.println("2. Kelola Data User (Petugas/Pelanggan)");
            System.out.println("3. Lihat Semua Transaksi");
            System.out.println("4. Tampilkan Info Saya");
            System.out.println("5. Logout");
            System.out.print("Pilih Menu: ");
            
            String pil = scanner.nextLine();
            switch (pil) {
                case "1": menuKelolaMobil(); break;
                case "2": menuKelolaUser(); break;
                case "3": menuLihatSemuaTransaksi(); break;
                case "4": userLogged.tampilkanInfo(); break;
                case "5": 
                    logout = true; 
                    userLogged.logout(); 
                    userLogged = null; 
                    break;
                default: System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private static void menuLihatSemuaTransaksi() {
        try {
            List<Booking> list = bookingDAO.findAll();
            System.out.println("\n--- RIWAYAT SEMUA TRANSAKSI ---");
            if (list.isEmpty()) System.out.println("Belum ada data transaksi.");
            for (Booking b : list) {
                System.out.println("ID:" + b.getIdBooking() + " | Pelanggan:" + b.getPelanggan().getNama() + 
                                   " | Mobil:" + b.getMobil().getModel() + " | Status:" + b.getStatus());
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void menuKelolaMobil() {
        System.out.println("\n--- SUB-MENU KELOLA MOBIL ---");
        System.out.println("1. Tambah Mobil Baru");
        System.out.println("2. Lihat Semua Mobil");
        System.out.println("3. Hapus Mobil (By ID)");
        System.out.println("4. Kembali");
        System.out.print("Pilih: ");
        String pil = scanner.nextLine();

        try {
            switch (pil) {
                case "1":
                    System.out.println("\n--- FORM TAMBAH MOBIL ---");
                    System.out.print("Merek Mobil      : "); String merek = scanner.nextLine();
                    System.out.print("Model/Tipe       : "); String model = scanner.nextLine();
                    System.out.print("Nomor Plat       : "); String plat = scanner.nextLine();
                    System.out.print("Tipe Mesin       : "); String mesin = scanner.nextLine();
                    System.out.print("Jumlah Kursi     : "); int kursi = Integer.parseInt(scanner.nextLine());
                    System.out.print("Harga Sewa/Hari  : "); double harga = Double.parseDouble(scanner.nextLine());
                    
                    Mobil baru = new Mobil(0, kursi, mesin, merek, model, plat, "Ready", harga);
                    mobilDAO.save(baru);
                    break;
                case "2":
                    List<Mobil> list = mobilDAO.findAll();
                    System.out.println("\n--- DAFTAR SEMUA MOBIL ---");
                    for (Mobil m : list) {
                        System.out.println("ID: " + m.getId() + " | " + m.getMerek() + " " + m.getModel() + " | Plat: " + m.getNomorPlat() + " | Status: " + m.getStatus());
                    }
                    break;
                case "3":
                    System.out.print("Masukkan ID Mobil yang akan dihapus: ");
                    int idHapus = Integer.parseInt(scanner.nextLine());
                    mobilDAO.delete(idHapus);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void menuKelolaUser() {
        System.out.println("\n--- SUB-MENU KELOLA USER ---");
        System.out.println("1. Lihat Daftar Pelanggan");
        System.out.println("2. Tambah Petugas Baru");
        System.out.println("3. Hapus User (By ID)");
        System.out.println("4. Kembali");
        System.out.print("Pilih: ");
        String pil = scanner.nextLine();

        try {
            switch (pil) {
                case "1":
                    List<User> list = userDAO.findAll();
                    System.out.println("\n--- DAFTAR USER ---");
                    for (User u : list) {
                        System.out.println("ID: " + u.getId() + " | Nama: " + u.getNama() + " | Email: " + u.getEmail());
                    }
                    break;
                case "2":
                    tambahPetugasOlehAdmin();
                    break;
                case "3":
                    System.out.print("Masukkan ID User yang akan dihapus: ");
                    int idHapus = Integer.parseInt(scanner.nextLine());
                    System.out.print("Ketik ROLE user tersebut (admin/petugas/pelanggan): ");
                    String roleHapus = scanner.nextLine();
                    userDAO.delete(idHapus, roleHapus);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
        }
    }

    private static void tambahPetugasOlehAdmin() throws SQLException {
        System.out.println("\n--- TAMBAH PETUGAS BARU ---");
        System.out.print("Nama    : "); String nama = scanner.nextLine();
        System.out.print("Email   : "); String email = scanner.nextLine();
        System.out.print("Password: "); String pass = scanner.nextLine();
        System.out.print("No Telp : "); String telp = scanner.nextLine();
        System.out.print("Shift   : "); String shift = scanner.nextLine();

        Petugas baru = new Petugas(0, nama, email, pass, telp, shift);
        userDAO.save(baru);
    }

    // =========================================================================
    // DASHBOARD PETUGAS
    // =========================================================================
private static void dashboardPetugas() {
    boolean logout = false;
    while (!logout) {
        System.out.println("\n=== DASHBOARD PETUGAS: " + userLogged.getNama() + " ===");
        System.out.println("1. Verifikasi Booking (Antrean)");
        System.out.println("2. Proses Pengembalian Mobil"); // Menu baru
        System.out.println("3. Tampilkan Info Saya");
        System.out.println("4. Logout");
        System.out.print("Pilih: ");
        String pil = scanner.nextLine();
        
        switch (pil) {
            case "1": menuVerifikasiBooking(); break;
            case "2": menuProsesPengembalian(); break; // Panggil method baru
            case "3": userLogged.tampilkanInfo(); break;
            case "4": logout = true; userLogged = null; break;
        }
    }
}

private static void menuProsesPengembalian() {
    try {
        System.out.println("\n--- DAFTAR MOBIL YANG SEDANG DISEWA ---");
        List<Booking> activeList = bookingDAO.findActiveBookings();

        if (activeList.isEmpty()) {
            System.out.println("Tidak ada mobil yang sedang disewa.");
            return;
        }

        for (Booking b : activeList) {
            System.out.println("ID Booking: " + b.getIdBooking() + 
                               " | Pelanggan: " + b.getPelanggan().getNama() + 
                               " | Mobil: " + b.getMobil().getModel() + 
                               " | Plat: " + b.getMobil().getNomorPlat());
        }

        System.out.print("\nMasukkan ID Booking untuk Pengembalian (0 untuk batal): ");
        int idB = Integer.parseInt(scanner.nextLine());

        if (idB != 0) {
            Booking bSelected = bookingDAO.findById(idB);
            if (bSelected != null) {
                // 1. Ubah status booking menjadi 'Completed' atau 'Selesai'
                bookingDAO.updateStatus(idB, "Completed");

                // 2. Kembalikan status mobil menjadi 'Ready'
                mobilDAO.updateStatus(bSelected.getMobil().getId(), "Ready");

                System.out.println("\n[SUCCESS] Mobil " + bSelected.getMobil().getModel() + " telah dikembalikan.");
                System.out.println("Status mobil sekarang: READY.");
            } else {
                System.out.println("[ERROR] ID Booking tidak ditemukan.");
            }
        }
    } catch (Exception e) {
        System.out.println("[ERROR] Gagal memproses pengembalian: " + e.getMessage());
    }
}

    private static void menuVerifikasiBooking() {
        try {
            System.out.println("\n--- ANTREAN BOOKING PELANGGAN (PENDING) ---");
            // PANGGILAN DAO UNTUK MENCARI DATA PENDING
            List<Booking> antrean = bookingDAO.findPendingBookings();
            
            if (antrean.isEmpty()) {
                System.out.println("Tidak ada antrean booking saat ini.");
                return;
            }

            for (Booking b : antrean) {
                System.out.println("ID Booking: " + b.getIdBooking() + 
                                   " | Pelanggan: " + b.getPelanggan().getNama() + 
                                   " | Mobil: " + b.getMobil().getMerek() + " " + b.getMobil().getModel() +
                                   " | Total: Rp" + b.getTotalBiaya());
            }

            System.out.print("\nMasukkan ID Booking yang akan diproses (0 untuk batal): ");
            int idB = Integer.parseInt(scanner.nextLine());

            if (idB != 0) {
                System.out.println("Tindakan: 1. Setujui (Approve) | 2. Tolak (Reject)");
                System.out.print("Pilih: ");
                String aksi = scanner.nextLine();
                
                String statusBaru = aksi.equals("1") ? "Approved" : "Rejected";
                
                // Ambil detail booking sebelum diupdate untuk mendapatkan mobil_id
                Booking bSelected = bookingDAO.findById(idB);
                
                if (bSelected != null) {
                    // 1. Update status booking di DB
                    bookingDAO.updateStatus(idB, statusBaru);
                    
                    // 2. Logika status mobil
                    if (aksi.equals("1")) {
                        mobilDAO.updateStatus(bSelected.getMobil().getId(), "Rented");
                        System.out.println("[SUCCESS] Booking ID " + idB + " disetujui. Mobil status: Rented.");
                    } else {
                        mobilDAO.updateStatus(bSelected.getMobil().getId(), "Ready");
                        System.out.println("[SUCCESS] Booking ID " + idB + " ditolak. Mobil status: Ready.");
                    }
                } else {
                    System.out.println("[ERROR] ID Booking tidak ditemukan.");
                }
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Gagal memproses verifikasi: " + e.getMessage());
        }
    }

    // =========================================================================
    // DASHBOARD PELANGGAN
    // =========================================================================
    private static void dashboardPelanggan() {
        boolean logout = false;
        while (!logout) {
            System.out.println("\n=== DASHBOARD PELANGGAN: " + userLogged.getNama() + " ===");
            System.out.println("1. Cari & Booking Mobil");
            System.out.println("2. Tampilkan Info Saya");
            System.out.println("3. Logout");
            System.out.print("Pilih: ");
            
            String pil = scanner.nextLine();
            switch (pil) {
                case "1": prosesBookingMobil(); break;
                case "2": userLogged.tampilkanInfo(); break;
                case "3": 
                    logout = true; 
                    userLogged.logout(); 
                    userLogged = null; 
                    break;
                default: System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private static void prosesBookingMobil() {
        try {
            System.out.println("\n--- DAFTAR MOBIL TERSEDIA (READY) ---");
            List<Mobil> listMobil = mobilDAO.findAvailableMobil();
            
            if (listMobil.isEmpty()) {
                System.out.println("Maaf, saat ini tidak ada mobil yang tersedia.");
                return;
            }

            for (Mobil m : listMobil) {
                System.out.println("ID: " + m.getId() + " | " + m.getMerek() + " " + m.getModel() + 
                                   " | Plat: " + m.getNomorPlat() + " | Harga: Rp" + m.getHargaSewaPerHari() + "/hari");
            }

            System.out.print("\nMasukkan ID Mobil yang ingin di-booking (0 untuk batal): ");
            int idMobil = Integer.parseInt(scanner.nextLine());
            
            if (idMobil != 0) {
                System.out.print("Tanggal Mulai (YYYY-MM-DD): ");
                String tglMulaiStr = scanner.nextLine();
                System.out.print("Tanggal Selesai (YYYY-MM-DD): ");
                String tglSelesaiStr = scanner.nextLine();
                
                LocalDate mulai = LocalDate.parse(tglMulaiStr);
                LocalDate selesai = LocalDate.parse(tglSelesaiStr);
                
                Mobil mSelected = mobilDAO.findById(idMobil);
                if (mSelected == null) {
                    System.out.println("Mobil tidak ditemukan!");
                    return;
                }

                Pelanggan p = (Pelanggan) userLogged;
                Booking baru = new Booking(0, mulai, selesai, mSelected, p);
                
                // Simpan ke DB
                bookingDAO.save(baru);
                
                // Ubah status mobil menjadi 'Booked'
                mobilDAO.updateStatus(idMobil, "Booked");

                System.out.println("\n[SUCCESS] Booking Berhasil Diajukan!");
                System.out.println("Total Biaya: Rp" + baru.getTotalBiaya());
                System.out.println("Status saat ini: PENDING. Silakan tunggu konfirmasi petugas.");
            }
            
        } catch (DateTimeParseException e) {
            System.out.println("[ERROR] Format tanggal salah! Gunakan YYYY-MM-DD (Contoh: 2025-12-30)");
        } catch (Exception e) {
            System.out.println("[ERROR] Booking gagal: " + e.getMessage());
        }
    }
}