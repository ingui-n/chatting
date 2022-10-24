import models.chatClients.ChatClient;
import models.chatClients.InMemoryChatClient;
import models.gui.MainFrame;

public class Main {
    public static void main(String[] args) {
        //testChat();

        ChatClient chatClient = new InMemoryChatClient();

        MainFrame window = new MainFrame(800, 600, chatClient);
    }

    private static void testChat() {
        ChatClient chatClient = new InMemoryChatClient();

        chatClient.login("me");
        chatClient.sendMessage("fgdfg");
        chatClient.logout();
    }
}