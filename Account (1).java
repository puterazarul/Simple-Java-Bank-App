// Account class: holds the data and the rules
public class Account {
    private String name;
    private String pin;
    private double balance;

    public Account(String name, String pin, double balance) {
        this.name = name;
        this.pin = pin;
        this.balance = balance;
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

    // Returns true if the deposit worked
    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        balance = balance + amount;
        return true;
    }

    // Returns true if the withdrawal worked
    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance = balance - amount;
        return true;
    }
}
