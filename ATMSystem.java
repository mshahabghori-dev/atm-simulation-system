import java.util.ArrayList;
import java.util.Scanner;

// Save the file as ATMSystem.java
public class ATMSystem {
    private static Scanner sc = new Scanner(System.in);
    private static ArrayList<BankAccount> accounts = new ArrayList<>();
    private static int nextAccountNumber = 1004;

    public static void main(String[] args) {
        // Demo accounts for testing
        accounts.add(new BankAccount("1001", "1234", "Arun", 5000));
        accounts.add(new BankAccount("1002", "2222", "Sara", 7500));
        accounts.add(new BankAccount("1003", "3333", "Rahul", 3000));

        System.out.println("================================");
        System.out.println("       ATM SIMULATION SYSTEM");
        System.out.println("================================");

        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = readInt();

            switch (choice) {
                case 1:
                    BankAccount account = login();
                    if (account != null) {
                        showMenu(account);
                    }
                    break;

                case 2:
                    register();
                    break;

                case 3:
                    System.out.println("Thank you for using the ATM.");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Find an account using its account number
    private static BankAccount findAccount(String accountNumber) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    // Create a new user account
    private static void register() {
        System.out.println("\n---------- REGISTRATION ----------");

        System.out.print("Enter your name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        System.out.print("Create a 4-digit PIN: ");
        String pin = sc.nextLine();

        if (!pin.matches("\\d{4}")) {
            System.out.println("PIN must contain exactly 4 digits.");
            return;
        }

        System.out.print("Confirm your PIN: ");
        String confirmPin = sc.nextLine();

        if (!pin.equals(confirmPin)) {
            System.out.println("PINs do not match. Registration cancelled.");
            return;
        }

        System.out.print("Enter initial deposit (enter 0 for none): Rs. ");
        double initialDeposit = readDouble();

        if (initialDeposit < 0) {
            System.out.println("Deposit cannot be negative.");
            return;
        }

        String accountNumber = String.valueOf(nextAccountNumber);
        nextAccountNumber++;

        BankAccount newAccount =
                new BankAccount(accountNumber, pin, name, initialDeposit);

        accounts.add(newAccount);

        System.out.println("\nRegistration successful!");
        System.out.println("Your account number is: " + accountNumber);
        System.out.println("Please remember your account number and PIN.");
    }

    // Check account number and PIN
    private static BankAccount login() {
        System.out.println("\n-------------- LOGIN --------------");

        System.out.print("Enter account number: ");
        String accountNumber = sc.nextLine();

        System.out.print("Enter PIN: ");
        String pin = sc.nextLine();

        BankAccount account = findAccount(accountNumber);

        if (account != null && account.checkPin(pin)) {
            System.out.println("Login successful. Welcome, " + account.getName() + "!");
            return account;
        }

        System.out.println("Invalid account number or PIN.");
        return null;
    }

    // Display the ATM operations
    private static void showMenu(BankAccount account) {
        while (true) {
            System.out.println("\n----------- ATM MENU -----------");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. Transaction History");
            System.out.println("6. Change PIN");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");

            int choice = readInt();

            switch (choice) {
                case 1:
                    System.out.printf("Available balance: Rs. %.2f%n",
                            account.getBalance());
                    break;

                case 2:
                    deposit(account);
                    break;

                case 3:
                    withdraw(account);
                    break;

                case 4:
                    transfer(account);
                    break;

                case 5:
                    account.showTransactionHistory();
                    break;

                case 6:
                    changePin(account);
                    break;

                case 7:
                    System.out.println("Logged out successfully.");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void deposit(BankAccount account) {
        System.out.print("Enter amount to deposit: Rs. ");
        double amount = readDouble();

        if (amount <= 0) {
            System.out.println("Amount must be greater than zero.");
            return;
        }

        account.deposit(amount);
        System.out.printf("Deposit successful. New balance: Rs. %.2f%n",
                account.getBalance());
    }

    private static void withdraw(BankAccount account) {
        System.out.print("Enter amount to withdraw: Rs. ");
        double amount = readDouble();

        if (amount <= 0) {
            System.out.println("Amount must be greater than zero.");
        } else if (amount > account.getBalance()) {
            System.out.println("Insufficient balance.");
        } else {
            account.withdraw(amount);
            System.out.printf("Please collect your cash. New balance: Rs. %.2f%n",
                    account.getBalance());
        }
    }

    private static void transfer(BankAccount sender) {
        System.out.print("Enter receiver's account number: ");
        String receiverNumber = sc.nextLine();

        BankAccount receiver = findAccount(receiverNumber);

        if (receiver == null) {
            System.out.println("Receiver account not found.");
            return;
        }

        if (receiver == sender) {
            System.out.println("You cannot transfer money to your own account.");
            return;
        }

        System.out.print("Enter amount to transfer: Rs. ");
        double amount = readDouble();

        if (amount <= 0) {
            System.out.println("Amount must be greater than zero.");
        } else if (amount > sender.getBalance()) {
            System.out.println("Insufficient balance.");
        } else {
            sender.transferOut(amount, receiverNumber);
            receiver.transferIn(amount, sender.getAccountNumber());

            System.out.println("Transfer successful.");
            System.out.printf("Your new balance: Rs. %.2f%n",
                    sender.getBalance());
        }
    }

    private static void changePin(BankAccount account) {
        System.out.print("Enter current PIN: ");
        String oldPin = sc.nextLine();

        if (!account.checkPin(oldPin)) {
            System.out.println("Incorrect current PIN.");
            return;
        }

        System.out.print("Enter new 4-digit PIN: ");
        String newPin = sc.nextLine();

        if (!newPin.matches("\\d{4}")) {
            System.out.println("PIN must contain exactly 4 digits.");
            return;
        }

        account.changePin(newPin);
        System.out.println("PIN changed successfully.");
    }

    // Read numbers safely
    private static int readInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Enter a valid number: ");
            sc.nextLine();
        }

        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }

    private static double readDouble() {
        while (!sc.hasNextDouble()) {
            System.out.print("Enter a valid amount: Rs. ");
            sc.nextLine();
        }

        double value = sc.nextDouble();
        sc.nextLine();
        return value;
    }
}

// Stores the details and operations of one bank account
class BankAccount {
    private String accountNumber;
    private String pin;
    private String name;
    private double balance;
    private ArrayList<String> transactions;

    public BankAccount(String accountNumber, String pin,
                       String name, double balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.name = name;
        this.balance = balance;
        transactions = new ArrayList<>();

        transactions.add("Account created. Opening balance: Rs. " + balance);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public boolean checkPin(String enteredPin) {
        return pin.equals(enteredPin);
    }

    public void deposit(double amount) {
        balance = balance + amount;
        transactions.add("Deposited Rs. " + amount);
    }

    public void withdraw(double amount) {
        balance = balance - amount;
        transactions.add("Withdrawn Rs. " + amount);
    }

    public void transferOut(double amount, String receiverNumber) {
        balance = balance - amount;
        transactions.add("Transferred Rs. " + amount
                + " to account " + receiverNumber);
    }

    public void transferIn(double amount, String senderNumber) {
        balance = balance + amount;
        transactions.add("Received Rs. " + amount
                + " from account " + senderNumber);
    }

    public void changePin(String newPin) {
        pin = newPin;
        transactions.add("PIN changed");
    }

    public void showTransactionHistory() {
        System.out.println("\n------ TRANSACTION HISTORY ------");

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        for (int i = 0; i < transactions.size(); i++) {
            System.out.println((i + 1) + ". " + transactions.get(i));
        }
    }
}