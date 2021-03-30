package Server;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class ServerThreadTest {
    ConnectedClients connectedClientsMock = Mockito.mock(ConnectedClients.class);
    SocketChannel socketChannelMock = Mockito.mock(SocketChannel.class);

    @Test
    public void broadcastSendTestOK() throws IOException {
        List<ServerThread> serversList = new LinkedList<>();
        for(int i=0;i<10;i++){
            serversList.add(new ServerThread(socketChannelMock,connectedClientsMock));
        }
        Mockito.when(socketChannelMock.write(ByteBuffer.wrap("test".getBytes(StandardCharsets.UTF_8)))).thenReturn(0);
        Mockito.when(connectedClientsMock.getServersList()).thenReturn(serversList);
        Assert.assertEquals("OK",serversList.get(0).broadcastSend("test"));
    }
}
