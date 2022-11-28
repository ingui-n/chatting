package models.chatClients.fileOperations;

import models.Message;

import java.util.List;

public interface ChatFileOperations {//todo csv operations
    void writeMessages(List<Message> messages);
    List<Message> readMessages();
}
