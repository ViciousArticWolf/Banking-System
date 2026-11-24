
import java.math.BigDecimal;
import java.util.*;

interface SubSystem {
    default void withdraw(Account acc) {}
    default void deposit(Account acc) {}
    default void transfer(Account acc) {}
    default void checkBalance(Account acc) {}
}

class Account {
    int id;
    String name;
    double balance;
    String password;

    Account(int id, String name, double balance, String password) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.password = password;
    }
}

class Withdraw implements SubSystem {
    @Override
    public void withdraw(Account acc) {
        System.out.println("Amount to be Withdrawn:");

        int amount = BankingSystem.sc.nextInt();

        if (amount <= 0) {
            System.out.println("Invalid Amount!");
        }
        else if (acc.balance < amount) {
            System.out.println("Insufficient Balance!");
        }
        else {
            acc.balance -= amount;
            System.out.println("Withdrawal Successful!");
            System.out.println("Current Balance: " + acc.balance);
        }
    }
}

class Deposit implements SubSystem {
    @Override
    public void deposit(Account acc) {
        System.out.println("Amount to be Deposited:");

        int amount = BankingSystem.sc.nextInt();

        if (amount <= 0) {
            System.out.println("Invalid Amount!");
        }
        else {
            acc.balance += amount;
            System.out.println("Deposit Successful!");
            System.out.println("Current Balance: " + acc.balance);
        }
    }
}

class Transfer implements SubSystem {
    @Override
    public void transfer(Account from) {
        System.out.println("Enter Account ID of Receiver:");

        int id = BankingSystem.sc.nextInt();

        if (!BankingSystem.accountExists(id)) {
            System.out.println("Invalid or Deleted Account!");
            return;
        }

        Account receiver = BankingSystem.accounts.get(id);

        if (receiver.id == from.id) {
            System.out.println("Cannot transfer to the same account!");
            return;
        }

        System.out.println("Enter Amount to be transferred:");

        int amount = BankingSystem.sc.nextInt();

        if (amount <= 0) {
            System.out.println("Invalid Amount!");
        }
        else if (from.balance < amount) {
            System.out.println("Insufficient Balance!");
        }
        else {
            from.balance -= amount;
            receiver.balance += amount;

            System.out.println("Transfer Successful!");
            System.out.println("Remaining Balance: " + from.balance);
        }
    }
}

class CheckBalance implements SubSystem {
    @Override
    public void checkBalance(Account acc) {
        System.out.println("Current Balance: " + acc.balance);
    }
}

public class BankingSystem {

    static ArrayList<Account> accounts = new ArrayList<>();
    static HashMap<Integer, String> credentials = new HashMap<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Welcome to Adbhut Ghaplebaaz Bank!!");

        choice();

        System.out.println("Thank You for using Adbhut Ghaplebaaz Bank!!");
    }

    // Checks whether an account ID exists and is not deleted.
    static boolean accountExists(int id) {
        return id >= 0
                && id < accounts.size()
                && accounts.get(id) != null;
    }

    // Account creation
    static void createAccount() {

        System.out.println("Enter Account holder Name:");
        String name = sc.next();

        System.out.println("Enter Account Balance:");
        double balance = sc.nextDouble();

        if (balance < 0) {
            System.out.println("Invalid Initial Balance!");
            return;
        }

        System.out.println("Enter Password:");
        String password = sc.next();

        int id = accounts.size();

        Account acc = new Account(id, name, balance, password);

        accounts.add(acc);
        credentials.put(id, password);

        System.out.println("Account Created Successfully!");
        System.out.println("Your Account ID is: " + id);
    }

    // Credential verification
    static boolean checkCredentials(int id, String password) {

        return credentials.containsKey(id)
                && password.equals(credentials.get(id));
    }

    // Authentication with retry, forgot password and cancel options.
    static boolean authenticate(int id) {

        if (!accountExists(id)) {
            System.out.println("Account doesn't exist!");
            return false;
        }

        while (true) {

            System.out.println("Enter Password:");
            String password = sc.next();

            if (checkCredentials(id, password)) {
                return true;
            }

            System.out.println("Wrong Credentials");
            System.out.println("Retry: Press 1");
            System.out.println("Forgot Password: Press 2");
            System.out.println("Cancel: Press 3");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    continue;

                case 2:
                    forgotPassword();
                    return false;

                case 3:
                    return false;

                default:
                    System.out.println("Invalid Choice!");
                    return false;
            }
        }
    }

    // Login into existing account
    static void switchAccount() {

        if (accounts.isEmpty()) {
            System.out.println("No Accounts registered yet!");
            return;
        }

        System.out.println("Enter Account holder ID:");
        int id = sc.nextInt();

        if (!accountExists(id)) {
            System.out.println("Account doesn't exist or has been deleted!");
            return;
        }

        if (authenticate(id)) {
            proceed(id);
        }
    }

    // Delete account
    static void deleteAccount() {

        if (accounts.isEmpty()) {
            System.out.println("No Accounts registered yet!");
            return;
        }

        System.out.println("Enter Account ID:");
        int id = sc.nextInt();

        if (!accountExists(id)) {
            System.out.println("Account doesn't exist or has been deleted!");
            return;
        }

        if (authenticate(id)) {
            accounts.set(id, null);
            credentials.remove(id);

            System.out.println("Account Deleted Successfully!");
        }
    }

    // Display all accounts
    static void showAccounts() {

        System.out.println("Accounts:");

        for (Account acc : accounts) {

            if (acc == null) {
                System.out.println("Deleted Account");
            }
            else {
                System.out.println(
                        acc.id + " " +
                                acc.name + " " +
                                acc.balance + " " +
                                acc.password
                );
            }
        }

        System.out.println("Size: " + accounts.size());
    }

    // Operations after successful login
    static void proceed(int id) {

        Account acc = accounts.get(id);

        while (true) {

            System.out.println("\nCheck Balance: Press 1");
            System.out.println("Deposit: Press 2");
            System.out.println("Withdraw: Press 3");
            System.out.println("Transfer: Press 4");
            System.out.println("Exit: Press 5");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    new CheckBalance().checkBalance(acc);
                    break;

                case 2:
                    new Deposit().deposit(acc);
                    break;

                case 3:
                    new Withdraw().withdraw(acc);
                    break;

                case 4:
                    new Transfer().transfer(acc);
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    // Forgot password placeholder
    static void forgotPassword() {
        System.out.println("Fuck Off!!");
    }

    // Main navigation menu
    static void choice() {

        while (true) {

            System.out.println("\nCreate Account: Press 1");
            System.out.println("Switch Account: Press 2");
            System.out.println("Delete Account: Press 3");
            System.out.println("Show Accounts: Press 4");
            System.out.println("To Exit: Press 5");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    createAccount();
                    break;

                case 2:
                    switchAccount();
                    break;

                case 3:
                    deleteAccount();
                    break;

                case 4:
                    showAccounts();
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
}
