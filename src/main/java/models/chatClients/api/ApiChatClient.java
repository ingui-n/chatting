package models.chatClients.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import models.Message;
import models.chatClients.ChatClient;
import models.chatClients.fileOperations.LocalDateTimeDeserializer;
import models.chatClients.fileOperations.LocalDateTimeSerializer;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ApiChatClient implements ChatClient {
    private String loggedUser;
    private List<String> loggedUsers;
    private List<Message> messages;
    private List<ActionListener> listeners = new ArrayList<>();

    private final String BASE_URL = "http://fimuhkpro22021.aspifyhost.cz/";
    private String token;
    private Gson gson;

    public ApiChatClient() {
        loggedUsers = new ArrayList<>();
        messages = new ArrayList<>();

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();

        Runnable refreshData = () -> {
            Thread.currentThread().setName("refreshData");

            try {
                while (true) {
                    if (isAuthenticated()) {
                        refreshLoggedUsers();
                        refreshMessages();
                    }

                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        Thread refreshDataThread = new Thread(refreshData);
        refreshDataThread.start();
    }

    @Override
    public void sendMessage(String text) {
        try {
            SendMessageRequest msgRequest = new SendMessageRequest(token, text);

            String url = BASE_URL + "/api/Chat/SendMessage";

            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity(gson.toJson(msgRequest), "utf-8");
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            if (response.getStatusLine().getStatusCode() == 204) {
                System.out.println("successful msg send");
                refreshMessages();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*messages.add(new Message(loggedUser, text));
        raiseEventMessagesChanged();*/
    }

    @Override
    public void login(String userName) {
        try {
            String url = BASE_URL + "/api/Chat/Login";

            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity("\"" + userName + "\"", "utf-8");
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            if (response.getStatusLine().getStatusCode() == 200) {
                System.out.println("successful login");

                token = EntityUtils.toString(response.getEntity());
                token = token.replace("\"", "").trim();

                loggedUser = userName;
                refreshLoggedUsers();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*loggedUser = userName;
        loggedUsers.add(userName);
        addSystemMessage(Message.USER_LOGGED_IN, userName);
        raiseEventLoggedUsersChanged();*/
    }

    @Override
    public void logout() {
        try {
            String url = BASE_URL + "/api/Chat/Logout";

            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity("\"" + token + "\"", "utf-8");
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            if (response.getStatusLine().getStatusCode() == 204) {
                System.out.println("successful logout");

                token = null;
                loggedUser = null;
                loggedUsers.clear();
                raiseEventLoggedUsersChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*loggedUsers.remove(loggedUser);
        addSystemMessage(Message.USER_LOGGED_OUT, loggedUser);
        loggedUser = null;
        raiseEventLoggedUsersChanged();*/
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
    }

    private void addSystemMessage(int type, String userName) {
        messages.add(new Message(type, userName));
        raiseEventMessagesChanged();
    }

    private void refreshLoggedUsers() {
        try {
            String url = BASE_URL + "/api/Chat/GetLoggedUsers";

            HttpGet get = new HttpGet(url);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(get);

            if (response.getStatusLine().getStatusCode() == 200) {
                String jsonResult = EntityUtils.toString(response.getEntity());
                loggedUsers = gson.fromJson(jsonResult, new TypeToken<ArrayList<String>>() {
                }.getType());
                raiseEventLoggedUsersChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshMessages() {
        try {
            String url = BASE_URL + "/api/Chat/GetMessages";

            HttpGet get = new HttpGet(url);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(get);

            if (response.getStatusLine().getStatusCode() == 200) {
                String jsonResult = EntityUtils.toString(response.getEntity());
                messages = gson.fromJson(jsonResult, new TypeToken<ArrayList<Message>>() {}.getType());
                raiseEventMessagesChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
