# Banking System - AI Coding Agent Guide

## Project Overview
A single-file ATM banking system (`src/Main.java`) with account management, transfers, and persistent storage. The system enforces Indian rupee denomination rules (₹100, ₹200, ₹500 only) and supports deposit, withdraw, transfer, and transaction history features.

## Architecture & Data Flow

### Core Components
- **`Account` class**: Represents user accounts with PIN authentication, balance tracking, and transaction history
  - Static `idCounter` manages sequential account IDs (starting 1001)
  - Must update counter on data load: `Account.updateCounter(maxId)`
  
- **`ATM` class**: Central coordinator maintaining account registry (`HashMap<Integer, Account>`)
  - Handles login validation, transfers, and data persistence
  - Performs denomination validation (called separately in `Account` and `ATM`)
  
- **`Main` class**: Console UI with two-menu flow (main menu → user operations menu)

### Critical Pattern: Denomination Validation
All monetary operations validate amounts through this logic:
```java
// Amount must be: positive, multiple of 100, and buildable from ₹100/₹200/₹500
if (amount <= 0 || amount % 100 != 0) throw error;
int amt = (int) amount;
amt %= 500; amt %= 200; amt %= 100;
if (amt != 0) throw error;  // Catches invalid combinations like ₹150
```
**Important**: This validation is duplicated in both `Account.validateAmount()` and `ATM.validateAmount()` — keep consistent when modifying.

### Data Persistence
- Format: Binary serialization (`java.io.ObjectInputStream/ObjectOutputStream`)
- File: `data/accounts.dat`
- Workflow: Load on ATM startup → Memory operations → Save on exit
- The `File` is created only if missing; directory created on first run

## Key Workflows

### 1. Account Creation
`ATM.createAccount(name, pin)` → Creates `Account` instance → Returns auto-incremented ID (1001+)

### 2. Login & Session
`ATM.login(accNo, pin)` → Returns Account if PIN valid, null otherwise → User menu loop starts

### 3. Money Transfer
`ATM.transfer(sender, receiverAccNo, amount)`:
- Validates denomination
- Prevents self-transfer
- Throws `NullPointerException` if receiver not found (caught as "Receiver account not found!")
- Updates both accounts' transaction history **separately**

### 4. Transaction Tracking
- `Account.transactionHistory` is `ArrayList<String>` with fixed format strings (e.g., "Deposited: ₹500")
- Transfers add two separate entries (one for sender, one for receiver)

## Exception Handling Strategy
- **Custom**: `InsufficientBalanceException` — domain-specific, caught at operation level
- **Standard**: `IllegalArgumentException` for denomination/logic violations, `NullPointerException` for missing receivers
- **I/O**: Caught silently in `saveData()`/`loadData()` (prints error, continues gracefully)
- All user-facing operations wrapped in try-catch with descriptive messages

## Conventions & Anti-Patterns

### DO:
- Validate amounts **before** modifying balance
- Add transactions **after** successful operations
- Use descriptive exception messages with denomination symbols (₹)
- Reload counter from persisted data to avoid ID collisions
- Catch exceptions at operation level, not globally

### DON'T:
- Modify balance directly; always use `deposit()`/`withdraw()`
- Assume receiver exists in transfer without null-check
- Save data mid-session (only on exit via `choice == 3`)
- Bypass denomination validation in any new monetary operation

## Testing & Debugging
- Manual CLI testing only — no unit tests in current structure
- Test denominations: ₹100, ₹200, ₹500, ₹1000, ₹2000
- Invalid test cases: ₹50, ₹150, ₹2.50, ₹0
- Verify account ID increment carries over after app restart
- Check transaction history formatting consistency

## File Reference
- Primary codebase: [src/Main.java](../src/Main.java)
- Persistence: `data/accounts.dat` (created automatically)
- Project config: `Banking system.iml` (IntelliJ metadata)
