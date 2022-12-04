package models.chatClients.fileOperations;

import models.Message;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvChatFileOperations implements ChatFileOperations {
    private static final String MESSAGES_FILE = "./messages.csv";
    private static final String splitter = ";";

    public CsvChatFileOperations() {

    }

    @Override
    public void writeMessages(List<Message> messages) {
        StringBuilder messagesBuilder = new StringBuilder();

        messagesBuilder.append("author" + splitter + "created" + splitter + "text" + "\n");

        for (Message message : messages) {
            String m = message.getAuthor() + splitter + message.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + splitter + message.getText() + "\n";
            messagesBuilder.append(m);
        }

        try {
            FileWriter writer = new FileWriter(MESSAGES_FILE);
            writer.write(messagesBuilder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Message> readMessages() {
        try {
            FileReader reader = new FileReader(MESSAGES_FILE);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String headLine = bufferedReader.readLine();

            if (headLine == null)
                return new ArrayList<>();

            List<String> head = Arrays.asList(headLine.split(splitter));
            int authorIndex = head.indexOf("author");
            int createdIndex = head.indexOf("created");
            int textIndex = head.indexOf("text");

            ArrayList<Message> messages = new ArrayList<>();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                List<String> lineArray = Arrays.asList(line.split(splitter));

                messages.add(new Message(lineArray.get(authorIndex), lineArray.get(textIndex), lineArray.get(createdIndex)));
            }

            reader.close();
            return messages;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
