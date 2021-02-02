package banking;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Account {

    private static long gid = 0;

    private final long id;
    private final Card card;
    private double balance = 0;

    public Account() {
        card = new Card();
        id = gid;
        gid++;
    }

    public Account(long id, double balance, String number, String pin) {
        this.balance = balance;
        this.card = new Card(number, pin);
        this.id = id;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (balance - amount >= 0) {
            balance -= amount;
            return true;
        }

        return false;
    }

    public long getId() {
        return id;
    }

    public Card getCard() {
        return card;
    }

    public double getBalance() {
        return balance;
    }
}

class Card {

    private final String number;
    private final String pin;

    public Card() {
        Random random = new Random();

        String bankIdentificationNumber = "400000";
        String accountIdentifier = String.format("%09d", Math.abs(random.nextLong() % 999999999L));
        number = bankIdentificationNumber + accountIdentifier + AccountUtils.calculateCheckSum(bankIdentificationNumber + accountIdentifier);
        pin = String.format("%04d", random.nextInt(10000));
    }

    public Card(String number, String pin) {
        this.number = number;
        this.pin = pin;
    }

    public String getNumber() {
        return number;
    }

    public String getPin() {
        return pin;
    }
}
