package banking;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class AccountUtils {

    public static Account getAccount(String cardNumber) {
        return Main.accounts.stream()
                .filter(acc -> acc.getCard().getNumber().equals(cardNumber))
                .findFirst()
                .orElse(null);
    }

    public static int calculateCheckSum(String number) {
        int sum = performLuhn(number);
        return (int) Math.ceil((double) sum / 10) * 10 - sum;
    }

    public static int performLuhn(String number) {
        AtomicInteger index = new AtomicInteger();
        return number.chars()
                .mapToObj(i -> (char) i)
                .mapToInt(Character::getNumericValue)
                .map(operand -> (index.getAndIncrement() + 1) % 2 != 0 ? operand * 2 : operand) // multiply odd indices by 2
                .map(operand -> operand > 9 ? operand - 9 : operand) // subtract 9 to numbers over 9
                .sum();
    }

    public static void transferMoney(Account sendingAccount) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Transfer\nEnter card number:");
        String receivingAccount = scanner.nextLine();
        if (receivingAccount.equals(sendingAccount.getCard().getNumber())) {
            System.out.println("You can't transfer money into the same account!\n");
            return;
        } else if (!(AccountUtils.performLuhn(receivingAccount) % 10 == 0)) {
            System.out.println("Probably you made mistake in the card number. Please try again!");
        }

        if (AccountUtils.getAccount(receivingAccount) != null) {
            System.out.println("Enter how much money you want to transfer:");
            int amount = Integer.parseInt(scanner.nextLine());

            if (sendingAccount.withdraw(amount)) {
                AccountUtils.getAccount(receivingAccount).deposit(amount);
                System.out.println("Success!\n");
            } else {
                System.out.println("Not enough money!\n");
            }

        } else {
            System.out.println("Such a card does not exist.");
        }
    }
}
