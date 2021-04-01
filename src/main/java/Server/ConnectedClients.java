package Server;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectedClients {
    private volatile List<ServerThread> serversList = new LinkedList<>();

    protected void addServer(ServerThread serverThread) {
        if (!serversList.contains(serverThread)) {
            serversList.add(serverThread);
        }
    }

    protected synchronized boolean isCorrectName(String nickName, ServerThread serverThread) {
        /*AtomicBoolean isNotFound = new AtomicBoolean(true);
        getServersList().forEach(s -> {
            if (s!=serverThread) {
                if (s.getSocketOwner() != null && s.getSocketOwner().equals(nickName)) {
                    isNotFound.set(false);
                }
            }
        });
        return isNotFound.get();*/
        return getServersList().stream()
                .filter(s -> s != serverThread)
                .map(s -> s.getSocketOwner())
                .filter(Objects::nonNull)
                .anyMatch(nickName::equals);
    }

    protected void remove(ServerThread serverThread) {
        serversList.remove(serverThread);
    }

    public List<ServerThread> getServersList() {
        return serversList;
    }
}
