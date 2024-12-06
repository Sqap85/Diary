import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DiaryManager {
    private final DatabaseManager dbManager;
    private int currentUserId = -1;

    public DiaryManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    // Kullanıcı kayıt işlemi
    public boolean register(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.err.println("Kullanıcı adı ve şifre boş olamaz.");
            return false;  // Return false if either is empty
        }

        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            System.out.println("Kayıt başarılı.");
            return true;
        } catch (SQLException e) {
            System.err.println("Kayıt sırasında hata: " + e.getMessage());
            return false;
        }
    }

    // Kullanıcı giriş işlemi
    public boolean login(String username, String password) {
        String sql = "SELECT id FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                currentUserId = rs.getInt("id");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Giriş sırasında hata: " + e.getMessage());
        }
        return false;
    }

    // Günlük ekleme
    public void addEntry(String title, String content) {
        String sql = "INSERT INTO diary_entries (user_id, title, content, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, currentUserId);
            pstmt.setString(2, title);
            pstmt.setString(3, content);
            pstmt.setString(4, LocalDate.now().toString());  // LocalDate.now() kullanıldı
            pstmt.executeUpdate();
            System.out.println("Günlük başarıyla eklendi.");
        } catch (SQLException e) {
            System.err.println("Günlük ekleme sırasında hata: " + e.getMessage());
        }
    }

    // Günlükleri görüntüleme
    public List<String> viewEntries(LocalDate startDate, LocalDate endDate) {
        List<String> entries = new ArrayList<>();
        String sql = "SELECT id, title, content, date FROM diary_entries WHERE user_id = ? AND date BETWEEN ? AND ? ORDER BY date";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, currentUserId);
            pstmt.setString(2, startDate.toString());
            pstmt.setString(3, endDate.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                entries.add("ID: " + rs.getInt("id") + ", Title: " + rs.getString("title") +
                        ", Date: " + rs.getString("date") + "\nContent: " + rs.getString("content"));
            }
        } catch (SQLException e) {
            System.err.println("Günlükler alınırken hata: " + e.getMessage());
        }
        return entries;
    }

    // Günlük silme
    public void deleteEntry(int entryId) {
        String sql = "DELETE FROM diary_entries WHERE id = ? AND user_id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, entryId);  // Silmek istenen günlük ID'sini veriyoruz
            pstmt.setInt(2, currentUserId);  // Kullanıcının ID'siyle karşılaştırıyoruz
            pstmt.executeUpdate();  // Silme işlemini yapıyoruz
            System.out.println("Günlük başarıyla silindi.");
        } catch (SQLException e) {
            System.err.println("Günlük silinirken hata: " + e.getMessage());
        }
    }

    // Günlük güncelleme
    public void updateEntry(int entryId, String newTitle, String newContent) {
        String sql = "UPDATE diary_entries SET title = ?, content = ? WHERE id = ? AND user_id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, newTitle);
            pstmt.setString(2, newContent);
            pstmt.setInt(3, entryId);
            pstmt.setInt(4, currentUserId);
            pstmt.executeUpdate();
            System.out.println("Günlük başarıyla güncellendi.");
        } catch (SQLException e) {
            System.err.println("Günlük güncellenirken hata: " + e.getMessage());
        }
    }
    // Başlığa göre arama yapma
    public List<String> searchEntriesByTitle(String title) {
        List<String> entries = new ArrayList<>();
        String sql = "SELECT id, title, content, date FROM diary_entries WHERE user_id = ? AND title LIKE ? ORDER BY date";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, currentUserId);
            pstmt.setString(2, "%" + title + "%"); // Başlığın herhangi bir kısmını eşleştir
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                entries.add("ID: " + rs.getInt("id") + ", Title: " + rs.getString("title") +
                        ", Date: " + rs.getString("date") + "\nContent: " + rs.getString("content"));
            }
        } catch (SQLException e) {
            System.err.println("Başlığa göre arama sırasında hata: " + e.getMessage());
        }
        return entries;
    }

    // Günlük ID'sine göre mevcut girdiyi alır
    public DiaryEntry getEntryById(int entryId) {
        String sql = "SELECT id, title, content, date FROM diary_entries WHERE id = ? AND user_id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, entryId);
            pstmt.setInt(2, currentUserId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new DiaryEntry(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        LocalDate.parse(rs.getString("date"))
                );
            }
        } catch (SQLException e) {
            System.err.println("Günlük verisi alınırken hata: " + e.getMessage());
        }
        return null; // Eğer bulamazsa null döner
    }

    // Oturumu kapatma
    public void logout() {
        currentUserId = -1;
    }
}
