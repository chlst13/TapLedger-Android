# TapLedger Android — alpha 0.1

**Status:** proyek sumber Android Studio, **bukan APK siap pakai**. Dibangun sebagai fondasi yang dapat diuji; fitur pembacaan saldo dan riwayat langsung dari chip **belum diterapkan**.

## Fungsi alpha yang benar-benar diimplementasikan
- Detect kartu NFC menggunakan Android Reader Mode (NfcA, NfcB, NfcF, NfcV).
- Menyimpan UID (bukan identitas aman) dan daftar teknologi kartu, waktu scan ke SQLite lokal.
- Multi kartu, hitungan scan, transaksi yang diinput manual, ringkasan pengeluaran manual.
- Ekspor satu catatan ke PDF bertanda **BUKAN BUKTI PEMBAYARAN RESMI**.
- Tidak memerlukan akun, server, atau internet.

## Cara build menjadi APK
1. Buka folder proyek ini di Android Studio versi yang mendukung Android Gradle Plugin 8.7.3, gunakan JDK 17+.
2. Biarkan Android Studio melakukan Gradle Sync dan memasang Android SDK Platform 35 bila diminta.
3. Pilih **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
4. APK debug akan tersedia di `app/build/outputs/apk/debug/app-debug.apk` **setelah build berhasil**.
5. Instal di Android 8+ yang memiliki NFC; aktifkan NFC; tempel kartu dari halaman aplikasi.

## Penting
- UID kartu bisa berubah, diduplikasi, atau sama antar kartu tertentu. Untuk identifikasi yang andal perlu metode spesifik penerbit.
- Aplikasi tidak otomatis mengetahui saldo, lokasi tol, gerbang masuk/keluar, penerbit kartu, ataupun riwayat transaksi dari chip. Tidak boleh membuat data palsu.
- Untuk pembacaan finansial: butuh spesifikasi APDU/format publik atau izin integrasi penerbit, implementasi per jenis/generasi kartu, uji pada kartu fisik nyata, dan penanganan status/duplikasi.
- Identitas Jasa Marga hanya dipakai saat ruas/operator benar-benar dapat diverifikasi. Logo brand memerlukan aset dan izin pemakaian yang sesuai.
- Struk manual merupakan catatan pribadi, bukan struk resmi penerbit.
- Perlu hardening lanjutan: database terenkripsi, backup ekspor terenkripsi, penghapusan data, pengujian UI, aksesibilitas, dan pemindaian keamanan.

## Roadmap
1. Adapter kartu teruji: Mandiri e-Money, Flazz, Brizzi, TapCash, KMT, JakCard, MegaCash (masing-masing dukungan bergantung generasi/protokol).
2. Deduplicated transaction ingestion + durable ledger across taps + transaction provenance and confidence.
3. Merchant/operator taxonomy + explicit verified roadway mapping + signed/static brand assets.
4. PDF/CSV export, sharing via FileProvider, filters/search, analytics, local encrypted storage, backups.
5. QA matrix physical devices and multiple card generations; signed APK release.

## Keamanan
READ ONLY; tidak mengirim perintah modifikasi saldo atau pembayaran. Tidak menyertakan kunci kriptografi kartu atau upaya melewati akses terenkripsi.


## GitHub Actions

Build debug APK otomatis dari workflow `.github/workflows/android-apk.yml`. Lihat `UPLOAD_GITHUB.md` untuk membuat repository dan mengunduh artifact APK.

Lihat `FEATURES.md` untuk status fitur yang tersedia dan keterbatasan NFC.

### Online toll receipt (first integration)
Open **Cari struk tol online** from Home or Card screen. The embedded official AstraTol website allows manual card-number/date search. After the real receipt appears, use **Simpan PDF** (Android Print > Save as PDF). No automated crawling, bulk scraping or transaction sync is claimed. The NFC tag UID is not necessarily the e-money card number.
