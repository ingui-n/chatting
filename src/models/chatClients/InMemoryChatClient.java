package models.chatClients;

import java.util.List;
import java.util.ArrayList;
import models.Message;

public class InMemoryChatClient implements ChatClient {
    private String loggedUser;
    private List<String> loggedUsers;
    private List<Message> messages;

    public InMemoryChatClient() {
        loggedUsers = new ArrayList<>();
        messages = new ArrayList<>();
    }

    @Override
    public void sendMessage(String text) {
        messages.add(new Message(loggedUser, text));
        System.out.println(text);
    }

    @Override
    public void login(String userName) {
        loggedUser = userName;
        loggedUsers.add(userName);
        System.out.println("logged out");
    }

    @Override
    public void logout(String userName) {
        loggedUsers.remove(loggedUser);

    }

    @Override
    public boolean isAuthentificated() {
        boolean isAuthed = loggedUser != null;
        System.out.println(isAuthed);
        return isAuthed;
    }

    @Override
    public List<String> getLoggedUsers() {
        return loggedUsers;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
