
import java.time.LocalDate;

public class DiaryEntry {
    private int entryID;
    private String title;
    private String content;
    private LocalDate date;

    public DiaryEntry(int entryID, String title, String content, LocalDate date) {
        this.entryID = entryID;
        this.title = title;
        this.content = content;
        this.date = date;
    }

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

    @Override
    public String toString() {
        return "ID: " + entryID + ", Title: " + title + ", Date: " + date + "\nContent: " + content;
    }
}
