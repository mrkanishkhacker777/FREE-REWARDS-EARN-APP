# GitHub Se APK + AAB Banane Ka Tarika
## Phone Se (Chrome Browser)

---

## STEP 1 — GitHub Account Banao
1. Chrome mein jao: https://github.com/signup
2. Email, password, username set karo
3. Account verify karo

---

## STEP 2 — New Repository Banao
1. github.com pe login karo
2. Top-right "+" icon → "New repository"
3. Repository name: `GetRedeemCode`
4. **Private** select karo (keystore safe rahe)
5. **"Create repository"** dabao

---

## STEP 3 — Files Upload Karo
1. "uploading an existing file" pe click karo
2. Saari files is zip se extract karke upload karo
   - `.github/` folder bhi upload karna hai!
3. "Commit changes" dabao

---

## STEP 4 — Secrets Set Karo (Password ke liye)
1. Repository → **Settings** → **Secrets and variables** → **Actions**
2. **"New repository secret"** dabao
3. Pehla secret:
   - Name: `STORE_PASSWORD`
   - Value: `GetRedeem@2024` (ya koi bhi strong password)
4. Dusra secret:
   - Name: `KEY_PASSWORD`  
   - Value: `GetRedeem@2024` (same password rakho)
5. Dono save karo

---

## STEP 5 — Build Start Karo
1. Repository → **Actions** tab
2. "Build APK and AAB" workflow dikhega
3. **"Run workflow"** → **"Run workflow"** dabao
4. ~10-15 minute wait karo

---

## STEP 6 — APK/AAB Download Karo
1. Actions → Completed build pe click karo
2. Neeche **Artifacts** section mein:
   - `Debug-APK` — testing ke liye
   - `Release-APK` — direct install ke liye
   - `Release-AAB-PlayStore` — Play Store ke liye
3. Click karke download karo!

---

## Notes
- Har baar code change karne pe automatic build hoga
- Files 30 din tak available rahenge
- Bilkul FREE — lifetime
