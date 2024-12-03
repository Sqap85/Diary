import javax.swing.*;
import java.awt.*;
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
        JButton deleteButton = new JButton("Delete Entry");
        JButton logoutButton = new JButton("Logout");

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(logoutButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);

        // Add Entry Action
        addButton.addActionListener(e -> {
            String title = JOptionPane.showInputDialog(this, "Entry Title:");
            String content = JOptionPane.showInputDialog(this, "Entry Content:");
            if (title != null && content != null) {
                manager.addEntry(title, content);
                updateDiaryList();
            }
        });

        // View Entries Action
        viewButton.addActionListener(e -> {
            List<String> entries = manager.viewEntries();
            diaryListModel.clear();
            entries.forEach(diaryListModel::addElement);
        });

        // Delete Entry Action
        deleteButton.addActionListener(e -> {
            int selectedIndex = diaryList.getSelectedIndex();
            if (selectedIndex >= 0) {
                String selectedEntry = diaryListModel.getElementAt(selectedIndex);
                try {
                    String entryIdStr = selectedEntry.split(",")[0].replace("ID:", "").trim();
                    if (!entryIdStr.isEmpty()) {
                        int entryId = Integer.parseInt(entryIdStr);
                        manager.deleteEntry(entryId);
                        updateDiaryList();
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid entry ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid entry ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an entry to delete.", "Error", JOptionPane.ERROR_MESSAGE);
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
        List<String> entries = manager.viewEntries();
        diaryListModel.clear();
        entries.forEach(diaryListModel::addElement);
    }
}