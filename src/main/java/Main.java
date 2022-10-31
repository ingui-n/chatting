import models.chatClients.ChatClient;
import models.chatClients.FileChatClient;
import models.chatClients.InMemoryChatClient;
import models.chatClients.fileOperations.ChatFileOperations;
import models.chatClients.fileOperations.JsonChatFileOperations;
import models.gui.MainFrame;

public class Main {
    public static void main(String[] args) {
        //testChat();
        ChatFileOperations chatFileOperations = new JsonChatFileOperations();

        ChatClient chatClient = new FileChatClient(chatFileOperations);

        MainFrame window = new MainFrame(800, 600, chatClient);
    }

    private static void testChat() {
        ChatClient chatClient = new InMemoryChatClient();

        chatClient.login("me");
        chatClient.sendMessage("fgdfg");
        chatClient.logout();
    }
}