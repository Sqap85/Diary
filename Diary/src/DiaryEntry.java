import java.time.LocalDate;

public class DiaryEntry {
    private int entryID;
    private String title;
    private String content;
    private LocalDate date;
    private String category;

    // Constructor
    public DiaryEntry(int entryID, String title, String content, LocalDate date, String category) {
        this.entryID = entryID;
        this.title = title;
        this.content = content;
        this.date = date;
        this.category = category;
    }

    // Getters and Setters
    public int getEntryID() {
        return entryID;
    }

    public void setEntryID(int entryID) {
        this.entryID = entryID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Method to return the string representation of the diary entry
    @Override
    public String toString() {
        return "ID: " + entryID + ", Title: " + title + ", Date: " + date + ", Category: " + category + "\nContent: " + content;
    }
}