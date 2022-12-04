import models.Message;
import models.chatClients.ChatClient;
import models.chatClients.FileChatClient;
import models.chatClients.InMemoryChatClient;
import models.chatClients.api.ApiChatClient;
import models.chatClients.fileOperations.ChatFileOperations;
import models.chatClients.fileOperations.CsvChatFileOperations;
import models.chatClients.fileOperations.JsonChatFileOperations;
import models.database.DatabaseOperations;
import models.database.DbInitializer;
import models.database.JdbcDatabaseOperations;
import models.gui.MainFrame;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String databaseDriver = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String databaseUrl = "jdbc:derby:ChatClientDb_skC";

    public static void main(String[] args) {
        ChatFileOperations chatFileOperations = new CsvChatFileOperations(); //new JsonChatFileOperations();

        ChatClient chatClient = new FileChatClient(chatFileOperations); //new ApiChatClient();
        MainFrame window = new MainFrame(800, 600, chatClient);
    }

    private static void testChat() {
        ChatClient chatClient = new InMemoryChatClient();

        chatClient.login("me");
        chatClient.sendMessage("fgdfg");
        chatClient.logout();
    }

    private static void initDb() {
        DbInitializer dbInitializer = new DbInitializer(databaseDriver, databaseUrl);
        dbInitializer.init();
    }

    private static void testDb() {
        try {
            DatabaseOperations databaseOperations = new JdbcDatabaseOperations(databaseDriver, databaseUrl);

            databaseOperations.addMessage(new Message("Dominik", "hi :)"));

            System.out.println(databaseOperations.getMessages());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** reflexe */
    /*Class<ApiChatClient> reflectionExample = ApiChatClient.class;
        System.out.println(reflectionExample.getSimpleName());

        for (Field f : getAllFields(reflectionExample)) {
            System.out.println(f.getName() + ":" + f.getType());
        }*/

    /*private static List<Field> getAllFields(Class<?> cls) {
        List<Field> fieldList = new ArrayList<>();

        for (Field f : cls.getDeclaredFields()) {
            fieldList.add(f);
        }

        return fieldList;
    }*/
}