package models;

import java.time.LocalDateTime;

public class Message {
    private String author;
    private String text;
    private LocalDateTime created;

    public static final int USER_LOGGED_IN = 1;
    public static final int USER_LOGGED_OUT = 2;
    public static final String AUTHOR_SYSTEM = "System";

    public Message(String author, String text) {
        this.author = author;
        this.text = text;
        this.created = LocalDateTime.now();
    }

    public Message(int type, String userName) {
        this.author = AUTHOR_SYSTEM;
        this.created = LocalDateTime.now();

        if (type == USER_LOGGED_IN) {
            text = userName + " has joined the chat";
        } else if (type == USER_LOGGED_OUT) {
            text = userName + " has left the chat";
        }
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }


    @Override
    public String toString() {
        if (author.equals(AUTHOR_SYSTEM))
            return text + "\n";

        return author + " [" + created.toLocalDate() + "]: " + text;
    }
}
