import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:sqlite:diary.db";
    private Connection connection;

    public DatabaseManager() {
        connect();
        createTables();  // Call to create tables when initializing the manager
    }

    // Veritabanına bağlan
    private void connect() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Veritabanına bağlanıldı.");
        } catch (SQLException e) {
            System.err.println("Veritabanı bağlantı hatası: " + e.getMessage());
        }
    }

    // Tabloları oluştur
    private void createTables() {
        String dropDiaryTable = "DROP TABLE IF EXISTS diary_entries;";  // Drop the old table if it exists

        String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE,
                    password TEXT
                );
                """;

        String createDiaryTable = """
                CREATE TABLE IF NOT EXISTS diary_entries (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER,
                    title TEXT,
                    content TEXT,
                    date TEXT,  -- Ensure 'date' column is added
                    FOREIGN KEY(user_id) REFERENCES users(id)
                );
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(dropDiaryTable);  // Drop the old diary_entries table if it exists
            stmt.execute(createUsersTable);
            stmt.execute(createDiaryTable);  // Create the table with the correct schema
            System.out.println("Tablolar başarıyla oluşturuldu.");
        } catch (SQLException e) {
            System.err.println("Tablolar oluşturulurken hata: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }
}