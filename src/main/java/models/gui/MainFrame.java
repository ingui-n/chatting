package models.gui;

import models.Message;
import models.chatClients.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    JTextArea txtChatArea;
    private ChatClient chatClient;
    JTextField txtInputMessage;

    public MainFrame(int width, int height, ChatClient chatClient) {
        super("Chatting");
        this.chatClient = chatClient;
        setSize(width, height);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initGui();
        setVisible(true);
    }

    private void initGui() {
        JPanel panelMain = new JPanel(new BorderLayout());

        panelMain.add(initLoginPanel(), BorderLayout.NORTH);
        panelMain.add(initChatPanel(), BorderLayout.CENTER);
        panelMain.add(initLoggedUsersPanel(), BorderLayout.EAST);
        panelMain.add(initMessagePanel(), BorderLayout.SOUTH);

        add(panelMain);
    }

    private JPanel initLoginPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel.add(new JLabel("Username"));

        JTextField txtUserName = new JTextField("", 30);
        panel.add(txtUserName);

        JButton btnLogin = new JButton("Login");

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = txtUserName.getText();
                System.out.println("logging " + userName);

                if (chatClient.isAuthenticated()) {
                    // logout
                    chatClient.logout();
                    btnLogin.setText("Login");
                    txtUserName.setEditable(true);
                    txtInputMessage.setEditable(false);
                    txtChatArea.setEnabled(false);
                } else {
                    // login
                    if (userName.length() < 1) {
                        JOptionPane.showMessageDialog(null, "Enter name", "Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    chatClient.login(userName);
                    btnLogin.setText("Logout");
                    txtUserName.setEditable(false);
                    txtInputMessage.setEditable(true);
                    txtChatArea.setEnabled(true);
                }
            }
        });

        panel.add(btnLogin);
        return panel;
    }

    private JPanel initChatPanel() {
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        txtChatArea = new JTextArea();
        txtChatArea.setEditable(false);
        txtChatArea.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(txtChatArea);
        panel.add(scrollPane);

//        for (int i = 0; i < 10; i++) {
//            txtChatArea.append("Message " + i);
//        }
        chatClient.addActionListener(e -> {
            if (e.getID() == 2)
                refreshMessages();
        });

        return panel;
    }

    private JPanel initMessagePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        txtInputMessage = new JTextField("", 50);
        txtInputMessage.setEditable(false);
        panel.add(txtInputMessage);

        JButton btnSendMessage = new JButton("Send");
        panel.add(btnSendMessage);

        btnSendMessage.addActionListener(e -> {
            String msgText = txtInputMessage.getText();
            System.out.println("send clicked " + txtInputMessage.getText());

            //txtChatArea.append(txtInputMessage.getText() + "\n");

            if (msgText.length() < 1)
                return;
            if (!chatClient.isAuthenticated())
                return;

            chatClient.sendMessage(msgText);
            txtInputMessage.setText("");
            //refreshMessages();
        });

        return panel;
    }

    private void refreshMessages() {
        if (!chatClient.isAuthenticated())
            return;

        txtChatArea.setText("");

        for (Message msg : chatClient.getMessages()) {
            txtChatArea.append(msg.toString());
            txtChatArea.append("\n");
        }
    }

    private JPanel initLoggedUsersPanel() {
        JPanel panel = new JPanel();

        /*Object[][] data = new Object[][]{
                {"0,0", "1,1"},
                {"s", "f"},
        };

        String[] colNames = new String[]{"col", "mel"};

        JTable tblLoggedUsers = new JTable(data, colNames);*/
        JTable tblLoggedUsers = new JTable();

        LoggedUsersTableModel loggedUsersTableModel = new LoggedUsersTableModel(chatClient);
        tblLoggedUsers.setModel(loggedUsersTableModel);

        chatClient.addActionListener(e -> {
            if (e.getID() == 1)
                loggedUsersTableModel.fireTableDataChanged();
        });

        JScrollPane scrollPane = new JScrollPane(tblLoggedUsers);
        scrollPane.setPreferredSize(new Dimension(250, 500));
        panel.add(scrollPane);

        return panel;
    }
}
