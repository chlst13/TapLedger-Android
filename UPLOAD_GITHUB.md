# Membuat repo GitHub dan APK TapLedger

1. Buka https://github.com/new saat login akun `chlst13`.
2. Buat repo baru bernama `TapLedger-Android`, pilih **Private**, centang **jangan** tambah README agar tidak konflik dengan paket source.
3. Upload isi **folder** TapLedgerAndroid (bukan ZIP secara utuh) ke repository; pastikan folder `.github/workflows` ikut terunggah. Cara paling stabil: GitHub Desktop / git CLI karena web upload kadang melewatkan hidden files.
4. Pada repo: **Actions > Android APK > Run workflow**. Push ke `main` juga memicu build.
5. Setelah build berhasil: buka run > **Artifacts** > `TapLedger-debug-apk`, unduh ZIP lalu ambil file `.apk` di dalamnya.
6. Aktifkan NFC dan install debug APK di perangkat Android yang dipercaya. Jangan menyebarkan debug build sebagai rilis publik.

### Git command alternatif
```
git init
git add .
git commit -m "Initial TapLedger Android alpha"
git branch -M main
git remote add origin https://github.com/chlst13/TapLedger-Android.git
git push -u origin main
```

**Batasan penting**: pemindaian saat ini hanya mengidentifikasi chip/tag dan tidak membaca saldo e-money bank. Struk PDF adalah catatan pribadi manual, bukan transaksi tersertifikasi.
