package models.chatClients;

import models.Message;
import models.chatClients.fileOperations.ChatFileOperations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class FileChatClient implements ChatClient {
    private String loggedUser;
    private List<String> loggedUsers;
    private List<Message> messages;
    private List<ActionListener> listeners = new ArrayList<>();

    private ChatFileOperations chatFileOperations;

    public FileChatClient(ChatFileOperations chatFileOperations) {
        this.chatFileOperations = chatFileOperations;
        loggedUsers = new ArrayList<>();
        messages = chatFileOperations.readMessages();
    }

    @Override
    public void sendMessage(String text) {
        messages.add(new Message(loggedUser, text));
        raiseEventMessagesChanged();
    }

    @Override
    public void login(String userName) {
        loggedUser = userName;
        loggedUsers.add(userName);
        addSystemMessage(Message.USER_LOGGED_IN, userName);
        raiseEventLoggedUsersChanged();
    }

    @Override
    public void logout() {
        loggedUsers.remove(loggedUser);
        addSystemMessage(Message.USER_LOGGED_OUT, loggedUser);
        loggedUser = null;
        raiseEventLoggedUsersChanged();
    }

    @Override
    public boolean isAuthenticated() {
        return loggedUser != null;
    }

    @Override
    public List<String> getLoggedUsers() {
        return loggedUsers;
    }

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public void addActionListener(ActionListener toAdd) {
        listeners.add(toAdd);
    }

    private void raiseEventLoggedUsersChanged() {
        for (ActionListener al : listeners) {
            al.actionPerformed(new ActionEvent(this, 1, "usersChanged"));
        }
    }

    private void raiseEventMessagesChanged() {
        for (ActionListener al : listeners) {
            al.actionPerformed(new ActionEvent(this, 2, "messagesChanged"));
        }

        chatFileOperations.writeMessages(messages);
    }

    private void addSystemMessage(int type, String userName) {
        messages.add(new Message(type, userName));
        raiseEventMessagesChanged();
    }
}
