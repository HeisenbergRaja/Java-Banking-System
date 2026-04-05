# Banking System - Java ATM Application

A simple console-based ATM banking system built in pure Java with in-memory account storage and transaction history tracking. This project demonstrates core OOP principles, data structure usage, and exception handling.

## 📋 Features

### Account Management
- **Create New Accounts** - Auto-generated account numbers starting from 1001
- **Account Holder Information** - Name and PIN authentication
- **Balance Tracking** - Real-time balance updates for all accounts
- **Persistent Storage** - Binary serialization for data persistence across sessions

### Banking Operations
- 💰 **Deposit Money** - Add funds to accounts with denomination validation
- 💸 **Withdraw Money** - Remove funds with insufficient balance checking
- 🔍 **Check Balance** - View current account balance
- 📊 **View Transaction History** - Complete record of all account transactions
- 🔄 **Transfer Money** - Send money between accounts with validation

### Security & Validation
- **PIN Authentication** - 4-digit PIN for account access
- **Denomination Validation** - Enforces Indian rupee denominations (₹100, ₹200, ₹500 only)
- **Input Validation** - Prevents invalid account numbers, negative amounts, and self-transfers
- **Balance Protection** - Prevents withdrawals exceeding available balance

## 🏗️ Architecture

### Account Structure
Each account contains:
```
Account Number (auto-generated, starting 1001)
├── Account Holder Name
├── PIN (4-digit authentication)
├── Balance (₹)
└── Transaction History (ArrayList)
```

### Data Structures Used
- **HashMap<Integer, Account>** - Stores all accounts with account number as key
- **ArrayList<String>** - Maintains transaction history for each account
- **Static idCounter** - Manages sequential account ID generation

### Core Classes

#### Account Class
- Manages individual account data and operations
- `validatePin()` - PIN authentication
- `deposit()` / `withdraw()` - Money operations with validation
- `showTransactions()` - Display transaction history
- Static `updateCounter()` - Reload ID counter on data load

#### ATM Class
- Central coordinator for account registry
- `createAccount()` - New account creation
- `login()` - Account authentication
- `transfer()` - Inter-account money transfer
- `saveData()` / `loadData()` - Persistence management

#### Main Class
- Console-based user interface
- Two-menu system: Main menu → User operations menu
- Exception handling for user inputs

## 🎯 Operations

### 1. Create Account
```
Input: Name, PIN
Output: Auto-generated Account Number (1001+)
```

### 2. Deposit Money
```
Input: Amount (must be multiple of 100, buildable from ₹100/₹200/₹500)
Validation: Amount > 0, amount % 100 == 0, denomination check
Result: Balance updated + Transaction logged
```

### 3. Withdraw Money
```
Input: Amount (same validation as deposit)
Validation: Amount <= Balance
Exception: InsufficientBalanceException if withdrawal exceeds balance
Result: Balance updated + Transaction logged
```

### 4. Check Balance
```
Output: Current account balance in ₹
```

### 5. View Transaction History
```
Output: Formatted list of all transactions
Format: "Deposited: ₹500" | "Withdrawn: ₹1000" | "Sent ₹500 to Acc No: 1002"
```

### 6. Transfer Money
```
Input: Receiver Account Number, Amount
Validation: 
  - Sender != Receiver (prevent self-transfer)
  - Amount follows denomination rules
  - Receiver account exists
  - Sender has sufficient balance
Result: Both accounts updated + separate transactions logged
```

## 🔒 Exception Handling

### Custom Exceptions
- **InsufficientBalanceException** - Thrown when withdrawal exceeds balance
  - Caught and displayed: "Insufficient Balance!"

### Standard Exceptions
- **IllegalArgumentException** - Invalid denominations or logic violations
  - Denomination error: "Use only ₹100, ₹200, ₹500 denominations!"
  - Amount error: "Amount must be in multiples of 100!"
  - Self-transfer error: "Cannot transfer to the same account!"

- **NullPointerException** - Receiver account not found in transfer
  - Caught and displayed: "Receiver account not found!"

### Input Validation
- All user inputs wrapped in try-catch blocks
- Scanner errors handled gracefully with user prompts

## 💾 Data Persistence

- **Format:** Binary serialization (ObjectInputStream/ObjectOutputStream)
- **Storage File:** `data/accounts.dat`
- **Workflow:**
  1. ATM loads all accounts on startup
  2. Users perform transactions in memory
  3. Data saved to disk when user exits (Choice 3 → Exit)
  4. Account ID counter reloaded to prevent ID collisions

**Important:** Data is only persisted on application exit. Closing the app unexpectedly will not save changes.

## ⚙️ Technical Stack

- **Language:** Java (Pure, no frameworks)
- **Storage:** In-memory HashMap + Binary file persistence
- **Input:** Scanner (console)
- **IDE:** IntelliJ IDEA (optional)

## 🎯 Constraints

✅ **No Database** - In-memory storage with file persistence only  
✅ **No Frameworks** - Pure Java implementation  
✅ **Input Validation** - All inputs validated for correctness  
✅ **Encapsulation** - Private fields with controlled access  
✅ **Exception Handling** - Custom and standard exceptions properly caught  

## 🚀 How to Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Command line terminal or IDE

### Compilation
```bash
cd "c:\Placement training\Banking system"
javac src/Main.java -d out/
```

### Execution
```bash
java -cp out/ Main
```

Or use your IDE to run the `Main` class directly.

## 📁 Project Structure

```
Banking system/
├── src/
│   └── Main.java           # Complete application (3 classes)
├── data/
│   └── accounts.dat        # Persistent storage (created automatically)
├── out/                    # Compiled .class files
├── .github/
│   └── copilot-instructions.md  # AI agent guidance
├── .idea/                  # IntelliJ configuration
├── Banking system.iml      # Project metadata
├── .gitignore             # Git ignore rules
└── README.md              # This file
```

## 📝 Denomination Validation Algorithm

All monetary amounts go through strict validation:

```
✓ Amount must be positive (> 0)
✓ Amount must be multiple of 100
✓ Amount must be buildable from ₹100, ₹200, ₹500

Examples:
  ✅ ₹100, ₹200, ₹500 - Valid single denominations
  ✅ ₹300 (₹100 + ₹200) - Valid combination
  ✅ ₹1000 (₹500 + ₹500) - Valid combination
  ✅ ₹2000, ₹1500 - Valid
  
  ❌ ₹50, ₹75, ₹150 - Invalid (cannot be built)
  ❌ ₹2.50 - Invalid (not multiple of 100)
  ❌ ₹0 - Invalid (must be positive)
  ❌ -₹100 - Invalid (must be positive)
```

## 🧪 Testing Examples

### Test Account Creation
```
Name: Raj Kumar
PIN: 1234
Account Number: 1001 (auto-generated)
```

### Test Deposit
```
Valid: ₹100, ₹500, ₹1000, ₹2000
Invalid: ₹150, ₹50, ₹75, ₹2.50
```

### Test Withdraw
```
Balance: ₹500
Valid Withdrawal: ₹200, ₹500
Invalid Withdrawal: ₹600 (insufficient balance)
```

### Test Transfer
```
Sender: Acc 1001 (Balance: ₹1000)
Receiver: Acc 1002
Amount: ₹200
Result: Sender balance ₹800, Receiver +₹200
```

### Session Persistence
```
1. Create account with balance ₹500
2. Exit app (saves data)
3. Restart app
4. Login to same account
5. Verify balance is still ₹500 ✓
```

## 🔐 Security Notes

- **No Encryption** - PINs stored in plaintext (for educational purposes)
- **Single User** - No multi-user concurrency support
- **No Password Strength** - PINs not validated for strength
- **Local Storage Only** - No network transmission

This is a learning project and should NOT be used for real banking applications.

## 📚 Key Learnings

This project demonstrates:
- ✅ Object-Oriented Programming (Classes, Encapsulation)
- ✅ Data Structures (HashMap, ArrayList, Static variables)
- ✅ Exception Handling (Custom exceptions, try-catch blocks)
- ✅ File I/O (Serialization/Deserialization)
- ✅ Input Validation (Business logic, numeric constraints)
- ✅ Console UI (Scanner-based interaction)
- ✅ Session Management (Login, logout workflows)

## 📖 Example Usage

```
===== ATM MENU =====
1. Create Account
2. Login & Continue
3. Exit
Enter your choice: 1

Enter your name: Raj Kumar
Set a 4-digit PIN: 1234
Account created successfully!
Your Account Number: 1001

===== ATM MENU =====
Enter your choice: 2

Enter your Account Number: 1001
Enter your PIN: 1234

--- Welcome Raj Kumar ---
1. Deposit
2. Withdraw
3. Check Balance
4. Transaction History
5. Transfer Money
6. Logout
Enter your choice: 1

Enter amount to deposit: 500
Current Balance: ₹500

Enter your choice: 2

Enter amount to withdraw: 200
Current Balance: ₹300

Enter your choice: 3

Current Balance: ₹300

Enter your choice: 6

Logged out successfully!
```

## 🤝 Contributing

This is an educational project. Feel free to fork, modify, and extend with additional features like:
- Account deletion
- Loan management
- Interest calculation
- Multi-user sessions
- Command-line arguments
- GUI using Swing/JavaFX

## 📄 License

Educational purpose - Open source

## ✨ Project Completion Status

- ✅ Account creation with auto-generated IDs
- ✅ Account information (name, balance, PIN)
- ✅ Deposit operations with validation
- ✅ Withdraw operations with balance checking
- ✅ Balance inquiry
- ✅ Transaction history tracking
- ✅ Money transfer between accounts
- ✅ Exception handling (custom and standard)
- ✅ Input validation
- ✅ Data persistence (binary serialization)
- ✅ HashMap for account storage
- ✅ ArrayList for transaction history
- ✅ Encapsulation and OOP principles

---

**Version:** 1.0  
**Last Updated:** April 5, 2026  
**Status:** Complete and Functional
