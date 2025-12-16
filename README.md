# 📚 Aplikasi Perpustakaan Digital

Aplikasi manajemen perpustakaan sederhana berbasis web yang dibangun menggunakan **Spring Boot** dan **Thymeleaf**. Aplikasi ini mendukung manajemen buku, peminjaman, dan pengelolaan pengguna dengan kontrol akses berbasis peran (Role-Based Access Control).

## 🚀 Fitur Utama

### 🔐 Otentikasi & Keamanan
-   **Login & Register**: Sistem pendaftaran dan masuk yang aman.
-   **Role-Based Access**:
    -   **ADMIN**: Akses penuh ke semua fitur (Manajemen Buku, User, Peminjaman).
    -   **VISITOR**: Akses terbatas untuk melihat katalog dan meminjam buku.
-   **Password Encryption**: Menggunakan BCrypt.

### 📖 Manajemen Buku (Admin)
-   **CRUD Buku**: Tambah, Edit, Hapus data buku.
-   **Upload Cover**: Upload gambar cover buku (disimpan lokal).
-   **Stok Manajemen**: Otomatis update stok saat dipinjam.

### 👤 Manajemen User (Admin)
-   **CRUD User**: Mengelola data pengguna dan role mereka.
-   **Pencarian**: Cari user berdasarkan nama atau username.

### 📚 Katalog & Peminjaman (Visitor)
-   **Katalog Buku**: Tampilan grid kartu yang responsif dengan pencarian.
-   **Multi-Borrow**: Meminjam beberapa buku sekaligus dalam satu transaksi.
-   **Riwayat**: (Coming Soon/In Progress)

### 📊 Dashboard
-   Statistik ringkas jumlah buku, peminjaman, dan user.

## 🛠️ Teknologi yang Digunakan

-   **Backend**: Java 23, Spring Boot 3.4.12
-   **Frontend**: Thymeleaf, Bootstrap 5, Bootstrap Icons
-   **Database**:
    -   MySQL (Development)
    -   PostgreSQL (Production / Render)
-   **Build Tool**: Gradle
-   **Containerization**: Docker

## ⚙️ Cara Menjalankan (Lokal)

### Prasyarat
-   Java JDK 23
-   MySQL Database

### Langkah-langkah
1.  **Clone Repository**
    ```bash
    git clone https://github.com/MuhammadSolikhin/springboot-perpus.git
    cd springboot-perpus
    ```

2.  **Konfigurasi Database**
    Edit file `src/main/resources/application.properties` sesuaikan dengan kredensial MySQL lokal Anda jika tidak menggunakan profile default/dev yang sudah ada.

3.  **Jalankan Aplikasi**
    ```bash
    ./gradlew bootRun
    ```
    Akses aplikasi di: `http://localhost:8080`

## 🐳 Cara Menjalankan (Docker)

1.  **Build Image**
    ```bash
    docker build -t perpus-app .
    ```

2.  **Run Container**
    ```bash
    docker run -p 8080:8080 perpus-app
    ```

---
Dibuat dengan ❤️
