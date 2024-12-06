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
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JLabel userLabel = new JLabel(" Username:");
        JLabel passLabel = new JLabel(" Password:");
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        panel.add(userLabel);
        panel.add(usernameField);
        panel.add(passLabel);
        panel.add(passwordField);
        panel.add(showPasswordCheckBox); // Checkbox ekleniyor
        panel.add(new JLabel()); // Layout uyumu için boş bir alan ekleniyor
        panel.add(loginButton);
        panel.add(registerButton);
        add(panel);

        // Show Password Action
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0); // Parola maskesi kaldırılıyor
            } else {
                passwordField.setEchoChar('•'); // Parola maskesi geri konuluyor
            }
        });

        // Login Action
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username or Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (manager.login(username, password)) {
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    openDiaryDashboard();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Register Action
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username Or Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (manager.register(username, password)) {
                    JOptionPane.showMessageDialog(this, "Registration Successful! Please log in.");
                    // Temizleme işlemi
                    usernameField.setText(""); // Kullanıcı adı kutusunu temizle
                    passwordField.setText(""); // Parola kutusunu temizle
                } else {
                    JOptionPane.showMessageDialog(this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void openDiaryDashboard() {
        DiaryDashboard dashboard = new DiaryDashboard(manager);
        dashboard.setVisible(true);
        this.dispose();
    }
}
