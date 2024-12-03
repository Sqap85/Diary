import javax.swing.*;

public class PersonalDiaryApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // DatabaseManager'ı başlat
            DatabaseManager dbManager = new DatabaseManager();

            // DiaryManager'ı başlat
            DiaryManager manager = new DiaryManager(dbManager);

            // Login ekranını başlat
            LoginScreen loginScreen = new LoginScreen(manager);
            loginScreen.setVisible(true);  // Login ekranını görünür yap
        });
    }
}