package Server;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectedClients {
    private List<ServerThread> serversList = new LinkedList<>();

    protected void addServer(ServerThread serverThread) {
        if (!serversList.contains(serverThread)) {
            serversList.add(serverThread);
        }
    }

    protected boolean isCorrectName(String msg, ServerThread serverThread) {
        AtomicBoolean isNotFound = new AtomicBoolean(true);
        serversList.forEach(s -> {
            if (s != serverThread) {
                if (s.getSocketOwner() != null && s.getSocketOwner().equals(msg)) {
                    isNotFound.set(false);
                }
            }
        });
        return isNotFound.get();
    }

    protected void remove(ServerThread serverThread) {
        if (serversList.contains(serverThread)) {
            serversList.remove(serverThread);
        }
    }

    public List<ServerThread> getServersList() {
        return serversList;
    }
}
