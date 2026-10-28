import javax.swing.*;
import java.awt.*;

// The window: a login screen and a bank screen (run main from here)
public class BankGUI extends JFrame {
    private Account account = new Account("Demo User", "1234", 1000.00);
    private int triesLeft = 3;

    private CardLayout cards = new CardLayout();
    private JPanel screens = new JPanel(cards);

    // login screen parts
    private JPasswordField pinField = new JPasswordField(10);
    private JLabel loginMessage = new JLabel(" ");

    // bank screen parts
    private JLabel balanceLabel = new JLabel();
    private JTextField amountField = new JTextField(10);
    private JLabel message = new JLabel(" ");
    private JTextArea history = new JTextArea(8, 30);

    public BankGUI() {
        super("Simple Bank");
        screens.add(makeLoginScreen(), "login");
        screens.add(makeBankScreen(), "bank");
        add(screens);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);   // open in the middle of the screen
    }

    // ---------- Build the screens ----------
    private JPanel makeLoginScreen() {
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> login());
        pinField.addActionListener(e -> login());        // Enter key also logs in

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.add(new JLabel("Welcome! Enter your PIN:", SwingConstants.CENTER));
        panel.add(pinField);
        panel.add(loginButton);
        panel.add(loginMessage);

        JPanel centered = new JPanel(new GridBagLayout());   // keeps the panel at its normal size
        centered.add(panel);
        return centered;
    }

    private JPanel makeBankScreen() {
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton exitButton = new JButton("Exit");
        depositButton.addActionListener(e -> deposit());
        withdrawButton.addActionListener(e -> withdraw());
        exitButton.addActionListener(e -> System.exit(0));

        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        history.setEditable(false);

        JPanel top = new JPanel(new GridLayout(0, 1, 5, 5));
        top.add(balanceLabel);
        top.add(new JLabel("Amount:"));
        top.add(amountField);

        JPanel buttons = new JPanel();
        buttons.add(depositButton);
        buttons.add(withdrawButton);
        buttons.add(exitButton);

        JPanel bottom = new JPanel(new GridLayout(0, 1));
        bottom.add(buttons);
        bottom.add(message);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(history), BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    // ---------- What the buttons do ----------
    private void login() {
        String pin = new String(pinField.getPassword());
        pinField.setText("");

        if (account.checkPin(pin)) {
            showBalance();
            cards.show(screens, "bank");
            return;
        }

        triesLeft--;
        if (triesLeft == 0) {
            JOptionPane.showMessageDialog(this, "Too many wrong tries. Card blocked.");
            System.exit(0);
        }
        loginMessage.setText("Wrong PIN. Tries left: " + triesLeft);
    }

    private void deposit() {
        double amount = readAmount();
        if (account.deposit(amount)) {
            addHistory("Deposit", amount);
            message.setText("Deposit successful.");
        } else {
            message.setText("Invalid amount.");
        }
        showBalance();
    }

    private void withdraw() {
        double amount = readAmount();
        if (account.withdraw(amount)) {
            addHistory("Withdraw", amount);
            message.setText("Please take your cash.");
        } else {
            message.setText("Invalid amount or not enough money.");
        }
        showBalance();
    }

    // ---------- Small helpers ----------
    // Reads the amount box. Returns -1 if it is not a proper number.
    private double readAmount() {
        try {
            double value = Double.parseDouble(amountField.getText().trim());
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                return -1;
            }
            return value;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void showBalance() {
        balanceLabel.setText(String.format("Balance: $%.2f", account.getBalance()));
        amountField.setText("");
    }

    private void addHistory(String type, double amount) {
        history.append(String.format("%-10s $%.2f%n", type, amount));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankGUI().setVisible(true));
    }
}
