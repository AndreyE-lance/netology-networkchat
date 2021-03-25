package Server;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

public class Server {
    final static Logger logger = Logger.getLogger("Server");
    final static String SETTINGS_PATH = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "settings.cfg";

    public static void main(String[] args) throws IOException {
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        FileReader fileReader = new FileReader(new File(SETTINGS_PATH));
        String[] cfg = null;

        try (BufferedReader bReader = new BufferedReader(fileReader)) {
            cfg = bReader.readLine().split(" ");
            serverChannel.bind(new InetSocketAddress(cfg[0], Integer.parseInt(cfg[1])));
            logger.debug("Сервер запущен");
            ConnectedClients connectedClients = new ConnectedClients();
            while (true) {
                connectedClients.addServer(new ServerThread(serverChannel.accept(), connectedClients));
            }
        } catch (IOException ex) {
            logger.error("Не удалось прочитать файл с настройками");
            System.out.println(ex.getMessage());
        }


    }
}
