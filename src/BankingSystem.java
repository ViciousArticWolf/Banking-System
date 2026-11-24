

import java.math.BigDecimal;
import java.util.*;
interface SubSystem{

    void run(Account account);
//    default void withdraw(Account acc){
//        System.out.println("Nothing to see here");
//    }
//    default void deposit(Account acc){}
//    default void transfer(Account from){}
//    default void checkBalance(Account acc){}
}
class Account{
    final int id;
    final String name;
    BigDecimal balance;
    String password;
    final String secretword;
    Account(int id, String name, BigDecimal balance, String password, String secretword){
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.password = password;
        this.secretword = secretword;
    }
}
class Withdraw implements SubSystem{
    @Override
    public void run(Account acc) {
        System.out.println("Amount to be Withdrawn:");
        BigDecimal a = BankingSystem.getBigDecimal();
        if(a.compareTo(BigDecimal.ZERO) <= 0){
            System.out.println("Invalid Amount");
        }
        else if (a.compareTo(acc.balance)>0) {
            System.out.println("Insufficient Balance!");
            System.out.println("Current Balance: " + acc.balance);
        } else {
            acc.balance = acc.balance.subtract(a);
            System.out.println("Withdraw Successful");
            System.out.println("Current Balance: " + acc.balance);
        }
    }
}
class Deposit implements SubSystem{
    @Override
    public void run(Account acc) {
        System.out.println("Amount to be Deposited");
        BigDecimal amount = BankingSystem.getBigDecimal();

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Invalid Amount!");
        } else {
            acc.balance = acc.balance.add(amount);
            System.out.println("Deposit Successful");
            System.out.println("Current Balance: "+ acc.balance);
        }
    }
}
class Transfer implements SubSystem{
    @Override

    public void run(Account from) {
        System.out.println("Enter Account ID of Receiver:");
        int toId = BankingSystem.getInt();
        Account to = BankingSystem.accounts.get(toId);
        if (to == null) {
            System.out.println(toId >= 0 && toId < BankingSystem.nextID
                    ? "Account has been deleted!"
                    : "Invalid Account ID!!");
            return;
        }
        if (to.id == from.id) {
            System.out.println("Cannot transfer to the same account!");
            return;
        }

        System.out.println("Enter Amount to be transferred:");
        BigDecimal b = BankingSystem.getBigDecimal();
        if (b.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Invalid Amount!");
        } else if (b.compareTo(from.balance) > 0) {
            System.out.println("Insufficient Balance!");
            System.out.println("Current Balance: " + from.balance);
        } else {
            from.balance = from.balance.subtract(b);
            to.balance = to.balance.add(b);
            System.out.println("Transfer Successful!");
            System.out.println("Your Balance: " + from.balance);
        }
    }
}

class CheckBalance implements SubSystem{
    @Override
    public void run(Account acc) {
        System.out.println("Current Balance: "+acc.balance);
    }
}

class InputEndedException extends RuntimeException {
}

public class BankingSystem {
    static final Map<Integer, Account> accounts = new HashMap<>();
    static Scanner sc = new Scanner(System.in);
    static int nextID = 0;
    public static void main(String[] args) {
        System.out.println("Welcome to Adbhut Ghaplebaaz Bank!!");
        try {
            mainLoop();
        } catch (InputEndedException e) {
            System.out.println("\nInput ended. Closing the bank safely.");
        }
        System.out.println("Thank You for using Adbhut Ghaplebaaz Bank!!");
    }

    public static void mainLoop() {
        while (true) {
            System.out.println("Create Account: Press 1");
            System.out.println("Switch Account: Press 2");
            System.out.println("Delete Account: Press 3");
            System.out.println("Show Accounts: Press 4");
            System.out.println("To Exit:  Press 5");
            int choice = getInt();
            if (choice == 5) return;
            handle(choice);
        }
    }

    static void handle(int choice) {
        switch (choice) {
            case 1 -> createAccount();
            case 2 -> switchAccount();
            case 3 -> deleteAccount();
            case 4 -> showAccounts();
            default -> System.out.println("Invalid choice!");
        }
    }

    static void createAccount(){
        System.out.println("Enter Account holder Name:");
        sc.nextLine();// Consume leftover newline from getInt()
        String name;
        while (true) {
            if (!sc.hasNextLine()) {
                throw new InputEndedException();
            }

            name = sc.nextLine();

            if (!name.trim().isEmpty()) {
                name = name.trim();
                break;
            }

            System.out.println("Name cannot be empty. Enter again:");
        }

        System.out.println("Enter Account Balance:");
        BigDecimal balance;
        while(true){
            balance = getBigDecimal();
            if(balance.compareTo(BigDecimal.ZERO) <= 0){
                System.out.println("Invalid Balance!");
            }
            else{
                break;
            }
        }

        System.out.println("Enter Password:(Must be of at least 6 characters and must have at least one special character, one Upper Case and one Lower Case character and no spaces.");

        String password = getPassword();

        System.out.println("Enter a secret word that you will remember:");
        String secretword;
        while (true) {
            if (!sc.hasNext()) {
                throw new InputEndedException();
            }
            secretword = sc.next();
            if(!secretword.trim().isEmpty()){
                break;
            }
        }

        int id = nextID;
        Account a = new Account(id, name, balance, password, secretword);
        accounts.put(id,a);
        System.out.println("Your account ID is:"+id);
        nextID++;
        //choice();
    }

//    static int getInt(){
//        while(!sc.hasNextInt()){
//            System.out.println("Invalid Input!");
//            sc.next();
//        }
//        return sc.nextInt();
//    }

    static int getInt() {
        while (true) {
            if (!sc.hasNext()) {
                throw new InputEndedException();
            }

            if (sc.hasNextInt()) {
                return sc.nextInt();
            }

            System.out.println("Invalid Input!");
            sc.next();
        }
    }


//    static BigDecimal getBigDecimal(){
//        while(!sc.hasNextBigDecimal()){
//            System.out.println("Invalid Input!");
//            sc.next();
//        }
//        return sc.nextBigDecimal();
//    }

    static BigDecimal getBigDecimal() {
        while (true) {
            if (!sc.hasNext()) {
                throw new InputEndedException();
            }
            if (sc.hasNextBigDecimal()) {
                return sc.nextBigDecimal();
            }
            System.out.println("Invalid Input!");
            sc.next();
        }
    }

    static void switchAccount(){
        Account a = login();
        if (a != null) {
            proceed(a);
        }
    }

    static void deleteAccount(){
        Account a = login();
        if(a!=null){
            accounts.remove(a.id);
            System.out.println("Your account with ID "+a.id+" has been deleted!");
        }
    }

    static Account login() {
        if(!accounts.isEmpty()){
            while (true) {
                System.out.println("Enter Account ID:");
                int id = getInt();
                if(accounts.containsKey(id)){
                    System.out.println("Enter Password:");
                    if (!sc.hasNext()) {
                        throw new InputEndedException();
                    }
                    String password = sc.next();
                    Account a = accounts.get(id);
                    if (a.password.equals(password)) {
                        return a;
                    }
                    else  {
                        System.out.println("Wrong Credentials");
                        System.out.println("Press 1 to retry");
                        System.out.println("Press 2 to change password");
                        System.out.println("Press 3 to exit");
                        int choice = getInt();
                        switch (choice) {
                            case 1 -> {
                                continue;
                            }
                            case 2 -> forgotPassword(id);
                            case 3 -> {
                                return  null;
                            }
                            default -> System.out.println("Invalid Choice! Press 1,2 or 3.");
                        }
                    }
                }
                else {
                    System.out.println(id >= 0 && id < nextID
                            ? "Account has been deleted!!"
                            : "Account doesnt exist!!");
                    System.out.println("Press 1 to retry");
                    System.out.println("Press 2 to exit");
                    int choice = getInt();
                    switch (choice) {
                        case 1 -> {}
                        case 2 -> {return null;}
                        default -> System.out.println("Invalid Choice! Press 1 or 2");
                    }
                }

            }
        }
        System.out.println("No Accounts registered yet");
        return null;
    }


    static void showAccounts(){
        System.out.println("Accounts:");
        for (int id = 0; id < nextID; id++) {
            Account a = accounts.get(id);
            if (a == null) {
                System.out.println("Account ID " + id + " - Deleted");
            } else {
                System.out.println(a.id + " " + a.name + " " + a.balance);//" " + a.password
            }
        }
        System.out.println("Size: " + accounts.size());

    }

    static void proceed(Account acc) {
        while (true) {
            System.out.println("Check Balance: Press 1");
            System.out.println("Deposit: Press 2");
            System.out.println("Withdraw: Press 3");
            System.out.println("Transfer: Press 4");
            System.out.println("Exit: Press 5");
            SubSystem s;
            switch (getInt()) {
                case 1 -> s = new CheckBalance();
                case 2 -> s = new Deposit();
                case 3 -> s = new Withdraw();
                case 4 -> s = new Transfer();
                case 5 -> { return; }
                default -> { System.out.println("Invalid choice!"); continue; }
            }
            s.run(acc);
        }
    }
    static String getPassword() {
        while (true) {
            if (!sc.hasNext()) {
                throw new InputEndedException();
            }
            String pwd = sc.next();
            if (pwd.length() < 6) {
                System.out.println("Password should be at least 6 characters!");
                continue;
            }
            if (pwd.matches(".*\\s+.*")) {
                System.out.println("Password should not contain spaces!");
                continue;
            }
            if (!pwd.matches(".*[A-Z].*")) {
                System.out.println("Password should contain at least one uppercase letter!");
                continue;
            }

            if (!pwd.matches(".*[a-z].*")) {
                System.out.println("Password should contain at least one lowercase letter!");
                continue;
            }
            if (!pwd.matches(".*[^a-zA-Z0-9].*")) {
                System.out.println("Password must contain at least one special character!");
                continue;
            }
            else {
                return pwd;
            }
        }

    }
    static void forgotPassword(int id) {
        Account a = accounts.get(id);
        boolean auth = Authenticate(a);
        if (auth) {
            System.out.println("Enter new Password:");
            a.password = getPassword();
            System.out.println("Password has been changed for account with ID:"+ a.id);
        }
        else{
            System.out.println("Sorry, Cant help you!");
            System.out.println("Please visit the bank for further assistance.");
        }
    }
    static boolean Authenticate(Account a) {
        System.out.println("Enter the secretword associated with the account:");
        if (!sc.hasNext()) {
            throw new InputEndedException();
        }
        String auth = sc.next();
        return a.secretword.equals(auth);
    }
}





