package Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.LinkedList;
import java.util.List;

public class Server {


    public static void main(String[] args) throws IOException {
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("192.168.0.108", 53535));
        ConnectedClients connectedClients = new ConnectedClients();
        while (true) {
            connectedClients.addServer(new ServerThread(serverChannel.accept(),connectedClients));
        }
    }
}
