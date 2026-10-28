import java.util.Scanner;

// ATM class: the screen and the menu (run main from here)
public class ATM {
    static Scanner input = new Scanner(System.in);
    static final int MAX_TRIES = 3;

    // Ask for a number until the user types a valid one
    static double readNumber(String message) {
        while (true) {
            System.out.print(message);
            String text = input.nextLine();
            try {
                return Double.parseDouble(text);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Give the user 3 tries to enter the correct PIN
    static boolean login(Account account) {
        for (int tries = 1; tries <= MAX_TRIES; tries++) {
            System.out.print("Enter PIN: ");
            String pin = input.nextLine();

            if (account.checkPin(pin)) {
                return true;
            }
            System.out.println("Wrong PIN. Tries left: " + (MAX_TRIES - tries));
        }
        return false;
    }

    static void showMenu() {
        System.out.println();
        System.out.println("1. Check balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Exit");
    }

    public static void main(String[] args) {
        Account account = new Account("Demo User", "1234", 1000.00);

        System.out.println("===== WELCOME TO THE ATM =====");

        if (!login(account)) {
            System.out.println("Too many wrong tries. Card blocked.");
            return;
        }

        System.out.println("Hello, " + account.getName() + "!");

        boolean running = true;
        while (running) {
            showMenu();
            int choice = (int) readNumber("Choose an option: ");

            if (choice == 1) {
                System.out.printf("Your balance is: $%.2f%n", account.getBalance());
            } else if (choice == 2) {
                double amount = readNumber("Amount to deposit: $");
                if (account.deposit(amount)) {
                    System.out.println("Deposit successful.");
                } else {
                    System.out.println("Invalid amount.");
                }
            } else if (choice == 3) {
                double amount = readNumber("Amount to withdraw: $");
                if (account.withdraw(amount)) {
                    System.out.println("Please take your cash.");
                } else {
                    System.out.println("Invalid amount or not enough money.");
                }
            } else if (choice == 4) {
                System.out.println("Thank you for using the ATM. Goodbye!");
                running = false;
            } else {
                System.out.println("Invalid option, try again.");
            }
        }
    }
}