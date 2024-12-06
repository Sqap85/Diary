import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class DiaryDashboard extends JFrame {
    private final DiaryManager manager;
    private final DefaultListModel<String> diaryListModel;

    public DiaryDashboard(DiaryManager manager) {
        this.manager = manager;
        setTitle("Personal Diary - Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        diaryListModel = new DefaultListModel<>();
        updateDiaryList();

        JList<String> diaryList = new JList<>(diaryListModel);
        JScrollPane scrollPane = new JScrollPane(diaryList);

        JButton addButton = new JButton("Add Entry");
        JButton viewButton = new JButton("View Entries");
        JButton updateButton = new JButton("Update Entry");
        JButton deleteButton = new JButton("Delete Entry");
        JButton logoutButton = new JButton("Logout");

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1));
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(logoutButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);

        // Add Entry Action
        addButton.addActionListener(e -> {
            while (true) {
                String title = JOptionPane.showInputDialog(this, "Entry Title:");
                if (title == null) {
                    // Kullanıcı çıkma tuşuna bastı, işlem iptal edilir
                    break;
                }
                if (title.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Title cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                // Başlık doğru girildiyse içerik isteme döngüsü
                while (true) {
                    String content = JOptionPane.showInputDialog(this, "Entry Content:");
                    if (content == null) {
                        // Kullanıcı çıkma tuşuna bastı, işlem iptal edilir
                        break;
                    }
                    if (content.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Content cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    // Başlık ve içerik doğru girildiyse ekleme yapılır
                    manager.addEntry(title, content);
                    updateDiaryList();
                    return; // Her iki giriş de tamamlanınca işlem sona erer
                }
            }
        });

        viewButton.addActionListener(e -> {
            // Kullanıcıya seçim yapmak için bir seçenek sun
            String[] options = {"Filter by Date", "View All", "Search by Title"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "How would you like to view the entries?",
                    "View Entries",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            switch (choice) {
                case 0: // Filter by Date
                    DateRangePicker datePicker = new DateRangePicker(this);
                    datePicker.setVisible(true);

                    if (datePicker.isConfirmed()) {
                        LocalDate startDate = datePicker.getStartDate();
                        LocalDate endDate = datePicker.getEndDate();

                        List<String> entries = manager.viewEntries(startDate, endDate);
                        diaryListModel.clear();
                        if (entries.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "No entries found for the selected date range.");
                        } else {
                            entries.forEach(diaryListModel::addElement);
                        }
                    }
                    break;

                case 1: // View All
                    List<String> allEntries = manager.viewEntries(LocalDate.MIN, LocalDate.now());
                    diaryListModel.clear();
                    if (allEntries.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "No entries found.");
                    } else {
                        allEntries.forEach(diaryListModel::addElement);
                    }
                    break;

                case 2: // Search by Title
                    String title = JOptionPane.showInputDialog(this, "Enter the title to search:");
                    if (title != null && !title.trim().isEmpty()) {
                        List<String> matchingEntries = manager.searchEntriesByTitle(title);
                        diaryListModel.clear();
                        if (matchingEntries.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "No entries found with the given title.");
                        } else {
                            matchingEntries.forEach(diaryListModel::addElement);
                        }
                    }
                    break;

                default:
                    // Kullanıcı bir seçim yapmadan çıktı
                    break;
            }
        });

        updateButton.addActionListener(e -> {
            int selectedIndex = diaryList.getSelectedIndex();
            if (selectedIndex >= 0) {
                // Seçilen öğeyi al
                String selectedEntry = diaryListModel.getElementAt(selectedIndex);
                String entryIdStr = selectedEntry.split(",")[0].replace("ID:", "").trim();
                int entryId = Integer.parseInt(entryIdStr);

                // Mevcut başlık ve içeriği almak
                String currentTitle = selectedEntry.split(",")[1].replace("Title:", "").trim();
                String currentContent = selectedEntry.split("\n")[1].replace("Content:", "").trim();

                // Başlık ve içerik için giriş kutuları göster
                String newTitle = JOptionPane.showInputDialog(this, "New Title:", currentTitle);
                if (newTitle == null) {
                    return; // Kullanıcı iptal etti
                }
                if (newTitle.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Title cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Başlık boş olamaz
                }

                String newContent = JOptionPane.showInputDialog(this, "New Content:", currentContent);
                if (newContent == null) {
                    return; // Kullanıcı iptal etti
                }
                if (newContent.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Content cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // İçerik boş olamaz
                }

                // Kullanıcı onayı almak için onay penceresi ekleyelim
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to update this entry?",
                        "Confirm Update",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Güncelleme işlemi
                    manager.updateEntry(entryId, newTitle, newContent);
                    updateDiaryList(); // Günlük listesi güncellenir
                    JOptionPane.showMessageDialog(this, "Entry updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No entry selected! Please select an entry to update.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Delete Entry Action
        deleteButton.addActionListener(e -> {
            int selectedIndex = diaryList.getSelectedIndex();
            if (selectedIndex >= 0) {
                String selectedEntry = diaryListModel.getElementAt(selectedIndex);
                String entryIdStr = selectedEntry.split(",")[0].replace("ID:", "").trim();
                int entryId = Integer.parseInt(entryIdStr);

                // Silme işlemi
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this entry?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    manager.deleteEntry(entryId);
                    updateDiaryList(); // Günlük listesi güncellenir
                }
            } else {
                JOptionPane.showMessageDialog(this, "No entry selected! Please select an entry to delete.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Logout Action
        logoutButton.addActionListener(e -> {
            manager.logout();
            LoginScreen loginScreen = new LoginScreen(manager);
            loginScreen.setVisible(true);
            this.dispose();
        });
    }

    private void updateDiaryList() {
        List<String> entries = manager.viewEntries(LocalDate.now().minusDays(30), LocalDate.now()); // Default to last 30 days
        diaryListModel.clear();
        entries.forEach(diaryListModel::addElement);
    }

}
