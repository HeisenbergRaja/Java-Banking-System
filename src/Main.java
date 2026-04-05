import java.util.*;
import java.io.*;

// Custom Exception
class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}

// Account Class
class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int idCounter = 1001;

    private int accountNumber;
    private String name;
    private int pin;
    private double balance;
    private ArrayList<String> transactionHistory;

    public Account(String name, int pin) {
        this.accountNumber = idCounter++;
        this.name = name;
        this.pin = pin;
        this.balance = 0;
        this.transactionHistory = new ArrayList<>();
    }

    public int getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getName() { return name; }

    public boolean validatePin(int inputPin) {
        return this.pin == inputPin;
    }

    // Denomination validation
    private void validateAmount(double amount) {
        if (amount <= 0 || amount % 100 != 0) {
            throw new IllegalArgumentException("Amount must be in multiples of 100!");
        }

        int amt = (int) amount;
        amt %= 500;
        amt %= 200;
        amt %= 100;

        if (amt != 0) {
            throw new IllegalArgumentException("Use only ₹100, ₹200, ₹500 denominations!");
        }
    }

    public void deposit(double amount) {
        validateAmount(amount);
        balance += amount;
        transactionHistory.add("Deposited: ₹" + amount);
    }

    public void withdraw(double amount) throws InsufficientBalanceException {
        validateAmount(amount);

        if (amount > balance) {
            throw new InsufficientBalanceException("Insufficient Balance!");
        }

        balance -= amount;
        transactionHistory.add("Withdrawn: ₹" + amount);
    }

    public void addTransaction(String msg) {
        transactionHistory.add(msg);
    }

    public void showTransactions() {
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }

        System.out.println("\n--- Transaction History ---");
        for (String t : transactionHistory) {
            System.out.println(t);
        }
    }

    public static void updateCounter(int maxId) {
        idCounter = maxId + 1;
    }
}

// ATM Class
class ATM {
    private HashMap<Integer, Account> accounts = new HashMap<>();
    private final String DIR = "data";
    private final String FILE_NAME = DIR + "/accounts.dat";

    public ATM() {
        createDataDirectory();
        loadData();
    }

    private void createDataDirectory() {
        File dir = new File(DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public int createAccount(String name, int pin) {
        Account acc = new Account(name, pin);
        accounts.put(acc.getAccountNumber(), acc);
        return acc.getAccountNumber();
    }

    public Account login(int accNo, int pin) {
        Account acc = accounts.get(accNo);
        if (acc != null && acc.validatePin(pin)) return acc;
        return null;
    }

    private void validateAmount(double amount) {
        if (amount <= 0 || amount % 100 != 0) {
            throw new IllegalArgumentException("Amount must be in multiples of 100!");
        }

        int amt = (int) amount;
        amt %= 500;
        amt %= 200;
        amt %= 100;

        if (amt != 0) {
            throw new IllegalArgumentException("Use only ₹100, ₹200, ₹500 denominations!");
        }
    }

    public void transfer(Account sender, int receiverAccNo, double amount)
            throws InsufficientBalanceException {

        if (sender.getAccountNumber() == receiverAccNo) {
            throw new IllegalArgumentException("Cannot transfer to the same account!");
        }

        validateAmount(amount);

        Account receiver = accounts.get(receiverAccNo);
        if (receiver == null) throw new NullPointerException();

        sender.withdraw(amount);
        receiver.deposit(amount);

        sender.addTransaction("Sent ₹" + amount + " to Acc No: " + receiverAccNo);
        receiver.addTransaction("Received ₹" + amount + " from Acc No: " + sender.getAccountNumber());
    }

    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(accounts);
        } catch (Exception e) {
            System.out.println("Error saving data!");
        }
    }

    @SuppressWarnings("unchecked")
    public void loadData() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No previous data found. Starting fresh.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            accounts = (HashMap<Integer, Account>) ois.readObject();

            int maxId = 1000;
            for (int id : accounts.keySet()) {
                if (id > maxId) maxId = id;
            }
            Account.updateCounter(maxId);

        } catch (Exception e) {
            System.out.println("Error loading data!");
        }
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ATM atm = new ATM();

        while (true) {
            System.out.println("\n===== ATM MENU =====");
            System.out.println("1. Create Account");
            System.out.println("2. Login & Continue");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            try {
                switch (choice) {

                    case 1:
                        sc.nextLine();
                        System.out.print("Enter your name: ");
                        String name = sc.nextLine();

                        System.out.print("Set a 4-digit PIN: ");
                        int pin = sc.nextInt();

                        int accNo = atm.createAccount(name, pin);
                        System.out.println("Account created successfully!");
                        System.out.println("Your Account Number: " + accNo);
                        break;

                    case 2:
                        System.out.print("Enter your Account Number: ");
                        accNo = sc.nextInt();

                        System.out.print("Enter your PIN: ");
                        pin = sc.nextInt();

                        Account user = atm.login(accNo, pin);
                        if (user == null) {
                            System.out.println("Invalid Account Number or PIN!");
                            break;
                        }

                        while (true) {
                            System.out.println("\n--- Welcome " + user.getName() + " ---");
                            System.out.println("1. Deposit");
                            System.out.println("2. Withdraw");
                            System.out.println("3. Check Balance");
                            System.out.println("4. Transaction History");
                            System.out.println("5. Transfer Money");
                            System.out.println("6. Logout");
                            System.out.print("Enter your choice: ");

                            int opt = sc.nextInt();

                            try {
                                if (opt == 1) {
                                    System.out.print("Enter amount to deposit: ");
                                    user.deposit(sc.nextDouble());
                                    System.out.println("Current Balance: ₹" + user.getBalance());

                                } else if (opt == 2) {
                                    System.out.print("Enter amount to withdraw: ");
                                    user.withdraw(sc.nextDouble());
                                    System.out.println("Current Balance: ₹" + user.getBalance());

                                } else if (opt == 3) {
                                    System.out.println("Current Balance: ₹" + user.getBalance());

                                } else if (opt == 4) {
                                    user.showTransactions();

                                } else if (opt == 5) {
                                    System.out.print("Enter receiver account number: ");
                                    int r = sc.nextInt();

                                    System.out.print("Enter amount to transfer: ");
                                    double amt = sc.nextDouble();

                                    atm.transfer(user, r, amt);
                                    System.out.println("Transfer successful!");
                                    System.out.println("Current Balance: ₹" + user.getBalance());

                                } else if (opt == 6) {
                                    System.out.println("Logged out successfully!");
                                    break;

                                } else {
                                    System.out.println("Invalid choice!");
                                }

                            } catch (InsufficientBalanceException | IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            } catch (NullPointerException e) {
                                System.out.println("Receiver account not found!");
                            }
                        }
                        break;

                    case 3:
                        atm.saveData();
                        System.out.println("Data saved. Exiting...");
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (Exception e) {
                System.out.println("Invalid input! Please try again.");
                sc.nextLine();
            }
        }
    }
}