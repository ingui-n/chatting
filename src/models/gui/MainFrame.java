package models.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    JTextArea txtChatArea;
    public MainFrame(int width, int height) {
        super("Chatting");
        setSize(width, height);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initGui();
        setVisible(true);
    }

    private void initGui() {
        JPanel panelMain = new JPanel(new BorderLayout());

        panelMain.add(initLoginPanel(), BorderLayout.NORTH);
        panelMain.add(initChatPanel(), BorderLayout.CENTER);
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
                System.out.println("logging " + txtUserName.getText());
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

        JScrollPane scrollPane = new JScrollPane(txtChatArea);
        panel.add(scrollPane);

//        for (int i = 0; i < 10; i++) {
//            txtChatArea.append("Message " + i);
//        }

        return panel;
    }

    private JPanel initMessagePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JTextField txtInputMessage = new JTextField("", 50);
        panel.add(txtInputMessage);

        JButton btnSendMessage = new JButton("Send");
        panel.add(btnSendMessage);

        btnSendMessage.addActionListener(e -> {
            System.out.println("send clicked " + txtInputMessage.getText());
            txtChatArea.append(txtInputMessage.getText() + "\n");
            txtInputMessage.setText("");
        });

        return panel;
    }
}
