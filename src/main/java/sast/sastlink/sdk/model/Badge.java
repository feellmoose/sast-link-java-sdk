package sast.sastlink.sdk.model;


import java.time.LocalDateTime;

public class Badge {
    String title;
    String description;
    LocalDateTime date;

    public Badge() {
    }

    public Badge(String title, String description, LocalDateTime date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public Badge setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Badge setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Badge setDate(LocalDateTime date) {
        this.date = date;
        return this;
    }
}
