
import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {
    private final DiaryManager manager;

    public LoginScreen(DiaryManager manager) {
        this.manager = manager;
        setTitle("Personal Diary - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Login Panel
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        panel.add(userLabel);
        panel.add(usernameField);
        panel.add(passLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);
        add(panel);

        // Login Action
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (manager.login(username, password)) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                openDiaryDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Register Action
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (manager.register(username, password)) {
                JOptionPane.showMessageDialog(this, "Registration Successful! Please log in.");
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void openDiaryDashboard() {
        DiaryDashboard dashboard = new DiaryDashboard(manager);
        dashboard.setVisible(true);
        this.dispose();
    }
}
