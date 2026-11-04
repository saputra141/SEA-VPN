# SEA VPN

Aplikasi Android SEA VPN saat ini berada pada tahap **demo UI**. Antarmuka menunjukkan status dan pilihan lokasi contoh, tetapi belum membuat koneksi internet melalui VPN.

## Status fitur

- Tampilan utama dan status koneksi demo.
- Pilihan lokasi contoh tersimpan di perangkat.
- Peringatan bahwa koneksi VPN belum aktif.
- Belum ada konfigurasi server atau tunnel VPN yang siap digunakan. Jangan menganggap perangkat terlindungi saat memakai build ini.

## Build

Proyek menggunakan Gradle Kotlin DSL, Android Gradle Plugin 8.9.1, Kotlin 2.1.20, dan Java 17. Buka proyek di Android Studio atau jalankan `gradle assembleDebug` setelah menyiapkan Android SDK serta submodule yang diperlukan.

File `app/libs/golib.aar` adalah dependensi biner lokal yang digunakan oleh modul aplikasi.
