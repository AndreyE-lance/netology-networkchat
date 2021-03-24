package Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ServerThread extends Thread {
    private SocketChannel socketChannel;
    private ConnectedClients connectedClients;
    private final ByteBuffer inputByteBuffer = ByteBuffer.allocate(2048);
    private String socketOwner;
    private boolean isFirstRun = true;

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
                if (bytesCount == -1) System.out.println("Клиент отвалился.");
            } catch (IOException e) {
                unexpectedDisconnect();
                e.printStackTrace();
                break;
            }

            final String msg = new String(inputByteBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
            if (msg.equals("*END*")) {
                closeSocket("*CONF_END*");
                broadcastSend("SYS: [" + socketOwner + "]: покинул чат.\n");
                break;
            }
            if (isFirstRun) {
                if (!connectedClients.isCorrectName(msg, this)) {
                    closeSocket("*WRONG_NAME*");
                    break;
                }
                isFirstRun = false;
                socketOwner = msg;
                inputByteBuffer.clear();
                broadcastSend("SYS: [" + socketOwner + "]: вошел в чат.\n");
            } else {
                inputByteBuffer.clear();
                broadcastSend("[" + socketOwner + "]: " + msg);
            }
        }

    }

    private void closeSocket(String msg) {
        try {
            connectedClients.remove(this);
            socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void unexpectedDisconnect() {
        connectedClients.remove(this);
        broadcastSend("SYS: [" + socketOwner + "]: обрыв связи.\n");
    }

    private void broadcastSend(String msg) {
        for (ServerThread server : connectedClients.getServersList()) {
            try {
                server.socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getSocketOwner() {
        return socketOwner;
    }
}

