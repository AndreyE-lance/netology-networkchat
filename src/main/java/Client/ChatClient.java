package Client;

import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ChatClient {
    private static JFrame frame;
    public JPanel panelMain;
    private JTextArea textArea2;
    private JButton sendMsgBtn;
    private JButton regBtn;
    private JTextField textField1;
    private JTextArea textArea1;
    private JLabel label1;
    private Client client;


    private String userName;
    final static String SETTINGS_PATH = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "settingsClient.cfg";
    static String[] cfg = null;

    public ChatClient() {
        sendMsgBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (client != null) {
                    if (!textArea2.getText().trim().isEmpty()) {
                        if (client.sendMsg(textArea2.getText() + "\n")) {
                            label1.setText("Сообщение отправлено");
                            textArea2.setText("");
                        } else {
                            label1.setText("Не удалось отправить сообщение");
                        }
                    } else {
                        label1.setText("Нельзя отпраить пустое сообщение");
                        textArea2.setText("");
                    }
                } else JOptionPane.showMessageDialog(frame, "Клиент не подключен к серверу.");
            }
        });

        regBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!textField1.getText().trim().isEmpty()) {
                    userName = textField1.getText();
                    client = new Client(textArea1, userName, cfg);
                    client.start();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    if (!client.isNameOk()) {
                        JOptionPane.showMessageDialog(frame, "Неверное имя.");
                    } else {
                        textField1.setVisible(false);
                        regBtn.setVisible(false);
                    }
                } else JOptionPane.showMessageDialog(frame, "Не введен никнейм для чата.");
            }
        });

        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                if (client != null) {
                    client.sendMsg("*END*");
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        textField1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField1.setText("");
            }
        });
    }

    public static void main(String[] args) throws IOException {
        frame = new JFrame("NIOChat");
        frame.setContentPane(new ChatClient().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        FileReader fileReader = new FileReader(new File(SETTINGS_PATH));
        try (BufferedReader bReader = new BufferedReader(fileReader)) {
            cfg = bReader.readLine().split(" ");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


}
