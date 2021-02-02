package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static boolean exit = false;

    static List<Account> accounts = new ArrayList<>();

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Error: You must enter a db name as a program argument (eg. accounts.db)");
            return;
        }

        DataSource.connect(args[0]);
        DataSource.loadData(accounts);

        while (!exit) {
            System.out.println("1. Create an account\n" +
                    "2. Log into an account\n" +
                    "0. Exit");

            switch (scanner.nextLine()) {
                case "1":
                    createAccount();
                    break;
                case "2":
                    accountLogin();
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println();
            }
        }

        DataSource.saveData(accounts);
        System.out.println("\nBye!");
    }

    public static void createAccount() {
        Account account = new Account();
        accounts.add(account);
        System.out.println("\nYour card has been created\n" +
                "Your card number:\n" +
                account.getCard().getNumber() + "\n" +
                "Your card PIN:\n" +
                account.getCard().getPin() + "\n");
    }

    public static void accountLogin() {
        System.out.println("\nEnter your card number:");
        String cardNumber = scanner.nextLine();

        Account account = AccountUtils.getAccount(cardNumber);

        System.out.println("Enter your PIN:");

        if (account == null || !account.getCard().getPin().equals(scanner.nextLine())) {
            System.out.println("\nWrong card number or PIN!");
            return;
        }

        System.out.println("\nYou have successfully logged in!\n");

        while (true) {
            System.out.println("1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n" +
                    "0. Exit");

            switch (scanner.nextLine()) {
                case "1":
                    System.out.println("\nBalance: " + String.format(account.getBalance() % 0.1 != 0 ?
                            "%s" : "%.0f", account.getBalance()) + "\n");
                    break;

                case "2":
                    System.out.println("Enter income:");
                    account.deposit(Integer.parseInt(scanner.nextLine()));
                    System.out.println("Income was added!\n");
                    break;

                case "3":
                    AccountUtils.transferMoney(account);
                    break;

                case "4":
                    accounts.remove(account);
                    System.out.println("The account has been closed!");
                    return;

                case "5":
                    System.out.println("\nYou have successfully logged out!\n");
                    return;

                case "0":
                    exit = true;
                    return;
            }
        }
    }
}
