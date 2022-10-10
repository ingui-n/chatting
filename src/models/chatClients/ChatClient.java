package models.chatClients;

import models.Message;

import java.util.List;


public interface ChatClient {
    void sendMessage(String text);
    void login(String userName);
    void logout(String userName);
    boolean isAuthentificated();

    List<Message> getMessages();
    List<String> getLoggedUsers();
}
