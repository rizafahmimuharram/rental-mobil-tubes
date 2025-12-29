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
import java.util.ArrayList;

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
        System.out.println("\n--- LAPORAN SEMUA TRANSAKSI & PENDAPATAN ---");
        // Mengambil semua data booking dari database
        List<Booking> semuaBooking = bookingDAO.findAll(); 

        if (semuaBooking.isEmpty()) {
            System.out.println("Belum ada data transaksi.");
            return;
        }

        double totalPendapatanMasuk = 0;
        double totalPotensiPendapatan = 0;

        System.out.println("--------------------------------------------------------------------------------------");
        System.out.printf("%-5s | %-15s | %-15s | %-12s | %-10s\n", 
                          "ID", "Pelanggan", "Mobil", "Total", "Status");
        System.out.println("--------------------------------------------------------------------------------------");

        for (Booking b : semuaBooking) {
            System.out.printf("%-5d | %-15s | %-15s | Rp%-10.0f | %-10s\n", 
                              b.getIdBooking(), 
                              b.getPelanggan().getNama(), 
                              b.getMobil().getModel(), 
                              b.getTotalBiaya(), 
                              b.getStatus());
            
            // Logika Pendapatan:
            // Uang dianggap masuk jika status sudah 'Paid', 'Approved', 'Rented', atau 'Completed'
            if (!b.getStatus().equalsIgnoreCase("Pending") && !b.getStatus().equalsIgnoreCase("Rejected")) {
                totalPendapatanMasuk += b.getTotalBiaya();
            }
            
            totalPotensiPendapatan += b.getTotalBiaya();
        }

        System.out.println("--------------------------------------------------------------------------------------");
        System.out.println("REKAP KEUANGAN ADMIN:");
        System.out.println("Total Pendapatan (Sudah Bayar/Selesai) : Rp " + totalPendapatanMasuk);
        System.out.println("Total Potensi Pendapatan (Termasuk Pending) : Rp " + totalPotensiPendapatan);
        System.out.println("--------------------------------------------------------------------------------------");
        
        System.out.println("\nTekan Enter untuk kembali ke menu...");
        scanner.nextLine();

    } catch (Exception e) {
        System.out.println("[ERROR] Gagal memuat laporan: " + e.getMessage());
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

    private static void dashboardPetugas() {
    boolean logout = false;
    while (!logout) {
        System.out.println("\n=== DASHBOARD PETUGAS: " + userLogged.getNama() + " ===");
        System.out.println("1. Verifikasi Ketersediaan (Status: Pending)"); // Tahap 1
        System.out.println("2. Verifikasi Pembayaran (Status: Paid)");     // Tahap 2 (Baru)
        System.out.println("3. Proses Pengembalian Mobil (Status: Rented)"); // Tahap 3
        System.out.println("4. Tampilkan Info Saya");
        System.out.println("5. Logout");
        System.out.print("Pilih: ");
        String pil = scanner.nextLine();
        
        switch (pil) {
            case "1": menuVerifikasiBooking(); break;
            case "2": menuVerifikasiPembayaran(); break; // Method baru di bawah
            case "3": menuProsesPengembalian(); break;
            case "4": userLogged.tampilkanInfo(); break;
            case "5": logout = true; userLogged = null; break;
        }
    }
}
    
    private static void menuVerifikasiPembayaran() {
    try {
        System.out.println("\n--- DAFTAR KONFIRMASI PEMBAYARAN (PAID) ---");
        // Kamu perlu buat method findByStatus di BookingDAO
        List<Booking> listPaid = bookingDAO.findByStatus("Paid");

        if (listPaid.isEmpty()) {
            System.out.println("Tidak ada pembayaran yang perlu diverifikasi.");
            return;
        }

        for (Booking b : listPaid) {
            System.out.println("ID Booking: " + b.getIdBooking() + 
                               " | Pelanggan: " + b.getPelanggan().getNama() + 
                               " | Total: Rp" + b.getTotalBiaya());
        }

        System.out.print("\nMasukkan ID Booking untuk Approve Pembayaran (0 batal): ");
        int idB = Integer.parseInt(scanner.nextLine());

        if (idB != 0) {
            Booking bSelected = bookingDAO.findById(idB);
            if (bSelected != null) {
                // Update Booking jadi Approved
                bookingDAO.updateStatus(idB, "Approved");
                // Update Mobil jadi Rented (Mobil resmi dibawa pelanggan)
                mobilDAO.updateStatus(bSelected.getMobil().getId(), "Rented");
                
                System.out.println("[SUCCESS] Pembayaran diterima. Mobil resmi berstatus RENTED.");
            }
        }
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
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
                
                Booking bSelected = bookingDAO.findById(idB);
                
                if (bSelected != null) {
                    bookingDAO.updateStatus(idB, statusBaru);
                  if (aksi.equals("1")) {
                        bookingDAO.updateStatus(idB, "Waiting Payment"); // Jangan langsung Rented
                        System.out.println("[SUCCESS] Booking disetujui. Menunggu pelanggan bayar.");
                    } else {
                        bookingDAO.updateStatus(idB, "Rejected");
                        mobilDAO.updateStatus(bSelected.getMobil().getId(), "Ready");
                        System.out.println("[SUCCESS] Booking ditolak.");
                    }
                } else {
                    System.out.println("[ERROR] ID Booking tidak ditemukan.");
                }
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Gagal memproses verifikasi: " + e.getMessage());
        }
    }

    
        //    ' ' ' DASHBOARD PELANGGAN ' ' '    
        private static void dashboardPelanggan() {
            boolean logout = false;
            while (!logout) {
                System.out.println("\n=== DASHBOARD PELANGGAN: " + userLogged.getNama() + " ===");
                System.out.println("1. Cari & Booking Mobil");
                System.out.println("2. Konfirmasi Pembayaran (Upload Bukti)");
                System.out.println("3. Riwayat Booking & Cetak Struk"); // Menu baru
                System.out.println("4. Tampilkan Info Saya");
                System.out.println("5. Logout");
                System.out.print("Pilih: ");

                String pil = scanner.nextLine();
                switch (pil) {
                    case "1": prosesBookingMobil(); break;
                    case "2": menuUploadPembayaran(); break;
                    case "3": menuRiwayatDanStruk(); break; // Method baru di bawah
                    case "4": userLogged.tampilkanInfo(); break;
                    case "5": logout = true; userLogged = null; break;
                }
            }
        }
    
private static void menuUploadPembayaran() {
    try {
        System.out.println("\n--- PEMBAYARAN BOOKING ---");
        List<Booking> myBookings = bookingDAO.findBookingsByPelanggan(userLogged.getId());
        
        if (myBookings.isEmpty()) {
            System.out.println("Anda tidak memiliki antrean pembayaran.");
            return;
        }
        List<Integer> validIds = new ArrayList<>();
        for (Booking b : myBookings) {
            System.out.println("ID: " + b.getIdBooking() + " | Total: Rp" + b.getTotalBiaya() + " | Status: " + b.getStatus());
            validIds.add(b.getIdBooking());
        }
        System.out.print("Masukkan ID Booking yang sudah dibayar: ");
        int idB = Integer.parseInt(scanner.nextLine());
        if (!validIds.contains(idB)) {
            System.out.println("[ERROR] ID Booking salah atau bukan milik Anda!");
            return; 
        }

        System.out.print("Masukkan Nama Bank / Referensi Transfer: ");
        String bukti = scanner.nextLine();

        bookingDAO.updateStatus(idB, "Paid");
        System.out.println("[SUCCESS] Konfirmasi pembayaran ID " + idB + " terkirim.");
        
    } catch (NumberFormatException e) {
        System.out.println("[ERROR] Masukkan angka untuk ID Booking!");
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
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
                bookingDAO.save(baru);
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
    
    
    
    private static void menuRiwayatDanStruk() {
    try {
        System.out.println("\n--- RIWAYAT BOOKING SAYA ---");
        List<Booking> myBookings = bookingDAO.findBookingsByPelanggan(userLogged.getId());

        if (myBookings.isEmpty()) {
            System.out.println("Anda belum memiliki riwayat booking.");
            return;
        }

        for (Booking b : myBookings) {
            System.out.println("ID: " + b.getIdBooking() + " | " + b.getMobil().getModel() + 
                               " | Status: " + b.getStatus() + " | Total: Rp" + b.getTotalBiaya());
        }

        System.out.print("\nMasukkan ID Booking untuk cetak struk (0 untuk kembali): ");
        int idB = Integer.parseInt(scanner.nextLine());

        if (idB != 0) {
            Booking bSelected = bookingDAO.findById(idB);
            if (bSelected != null && bSelected.getPelanggan().getId() == userLogged.getId()) {
                cetakStruk(bSelected);
            } else {
                System.out.println("[ERROR] Data tidak ditemukan atau akses ditolak.");
            }
        }
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}
    
private static void cetakStruk(Booking b) {
    System.out.println("\n==========================================");
    System.out.println("           NOTA RENTAL MOBIL              ");
    System.out.println("==========================================");
    System.out.println(" ID Booking   : " + b.getIdBooking());
    System.out.println(" Tanggal      : " + LocalDate.now());
    System.out.println("------------------------------------------");
    System.out.println(" Pelanggan    : " + b.getPelanggan().getNama());
    System.out.println(" Mobil        : " + b.getMobil().getMerek() + " " + b.getMobil().getModel());
    System.out.println(" Plat Nomor   : " + b.getMobil().getNomorPlat());
    System.out.println(" Durasi Sewa  : " + b.getTanggalMulai() + " s/d " + b.getTanggalSelesai());
    System.out.println("------------------------------------------");
    System.out.println(" HARGA SEWA   : Rp" + b.getMobil().getHargaSewaPerHari() + " /hari");
    System.out.println(" TOTAL BAYAR  : Rp" + b.getTotalBiaya());
    System.out.println(" STATUS       : " + b.getStatus());
    System.out.println("==========================================");
    System.out.println("     Terima Kasih Atas Kunjungan Anda     ");
    System.out.println("    Harap Simpan Struk Ini Sebagai Bukti  ");
    System.out.println("==========================================\n");
}
}