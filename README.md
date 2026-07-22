# 📈 Stock Portfolio Application

A robust Java desktop application designed to model a financial brokerage account. The engine features an automated dual-entry transaction ledger, real-time dynamic portfolio calculation, and strict liquidity validation safeguards.

---

### 🚀 Core Features
* **💵 Liquidity Management:** Handles secure balance validation for capital deposits and withdrawals.
* **💹 Asset Execution Engine:** Automatically processes stock purchases and sales.
* **🧾 Dual-Entry Ledger:** Executing a stock trade creates paired transaction objects (Asset ledger update + Cash ledger offset) to preserve absolute data integrity.
* **📊 Dynamic Portfolio Views:** Real-time collection reduction loops to aggregate transaction histories into current net holdings.

---

### 🏗️ Technical Architecture
The system architecture isolates system mutations using Object-Oriented Programming (OOP) paradigms:
* `TransactionHistory.java` - A tightly encapsulated data entity holding transaction attributes (`ticker`, `transDate`, `transType`, `qty`, `costBasis`) accessible via explicit getters and setters.
* `PortfolioManager.java` - The main driver class governing input verification, the interactive console loop, and state synchronization.

---

### 🛑 Contribution Policy
> ⚠️ **Academic Integrity Notice:** This project was developed as a course assignment. **Contributions cannot be accepted**, and copying this codebase may violate your institution's academic integrity policies.

---

### 🎓 Academic Context
* **Project:** Final Capstone Project
* **Author:** Sarah Moore
* **Course:** IFT 210: Intro to Java Technologies
* **Institution:** Arizona State University
