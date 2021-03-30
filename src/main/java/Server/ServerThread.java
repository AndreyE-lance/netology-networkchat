package Server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerThread extends Thread {
    private final SocketChannel socketChannel;
    private final ConnectedClients connectedClients;
    private final ByteBuffer inputByteBuffer = ByteBuffer.allocate(2048);
    private String socketOwner;
    private boolean isFirstRun = true;
    final static String HISTORY_PATH = "src" + File.separator + "history" + File.separator + "History.log";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");


    public ServerThread(SocketChannel socketChannel, ConnectedClients connectedClients) {
        this.connectedClients = connectedClients;
        this.setDaemon(true);
        this.socketChannel = socketChannel;
        start();
    }

    @Override
    public void run() {
        while (true) {
            int bytesCount = 0;
            try {
                bytesCount = socketChannel.read(inputByteBuffer);
            } catch (IOException e) {
                unexpectedDisconnect();
                Server.logger.trace(e.getMessage());
                e.printStackTrace();
                break;
            }

            String msg = new String(inputByteBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
            if (msg.equals("*END*")) {
                closeSocket("*CONF_END*");
                Server.logger.debug(socketOwner + " покинул чат. Поток " + Thread.currentThread().getName() + " закрыт.");
                broadcastSend("SYS: [" + socketOwner + "]: покинул чат.");
                break;
            }
            if (isFirstRun) {
                if (!connectedClients.isCorrectName(msg, this)) {
                    closeSocket("*WRONG_NAME*");
                    Server.logger.error("Подключиться не удалось, т.к. имя " + msg + " уже используется в чате.");
                    break;
                } else {
                    Server.logger.debug("Подключился новый пользователь с именем " + msg);
                    Server.logger.debug("Поток " + Thread.currentThread().getName() + " для пользователя " + msg + " успешно создан.");
                }
                isFirstRun = false;
                socketOwner = msg;
                inputByteBuffer.clear();
                broadcastSend("SYS: [" + socketOwner + "]: вошел в чат.\n");
            } else {
                inputByteBuffer.clear();
                logHistory("[" + socketOwner + "]: " + msg);
                broadcastSend("[" + socketOwner + "]: " + msg);
            }
        }

    }

    private void closeSocket(String msg) {
        try {
            connectedClients.remove(this);
            socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            Server.logger.trace(e.getMessage());
            e.printStackTrace();
        }
    }

    private void unexpectedDisconnect() {
        connectedClients.remove(this);
        broadcastSend("SYS: [" + socketOwner + "]: обрыв связи.\n");
    }

    private synchronized void logHistory(String msg) {
        try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(HISTORY_PATH, true))) {
            bWriter.write(dateFormat.format(new Date()) + " : " + msg);
            bWriter.flush();
        } catch (IOException e) {
            Server.logger.trace(e.getMessage());
            e.printStackTrace();
        }
    }


    protected String broadcastSend(String msg) {
        for (ServerThread server : connectedClients.getServersList()) {
            try {
                server.socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
            } catch (IOException e) {
                Server.logger.trace(e.getMessage());
                return "ERROR";
            }
        }
        return "OK";
    }

    public String getSocketOwner() {
        return socketOwner;
    }
}

