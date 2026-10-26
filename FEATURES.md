# TapLedger capability matrix

## Implemented (local alpha)
- Android NFC Reader Mode; discover tag UID and compatible tech types only.
- Local encrypted? NO: SQLite database stored inside application sandbox, NOT encrypted.
- Multi-card registry with last scan time.
- Manual expense/top-up transaction logging with categories, merchant, location, operator and note.
- Rename NFC cards and export transaction history to CSV via Android document picker.
- Local history and spend total (manual records only).
- PDF personal receipt (not bank/operator proof of payment).
- Offline, no network permission.
- GitHub Actions build of installable unsigned-for-publishing, debug-signed APK artifact.

## Not implemented / requires card-specific legal documentation, access or real hardware
- Verified bank-card balances and on-card transaction reads for Mandiri e-Money, Flazz, Brizzi, TapCash, JakCard, KMT, etc.
- Toll gate ID/tariff/operator resolution and official logo licensing.
- Official payment receipts; cloud sync; biometrics; NFC security hardening; top-ups.
- Excel export, full search/filter, analytics charts, backup and restore.

Do not treat UID as authentication. Avoid transmitting or publishing raw card IDs.
