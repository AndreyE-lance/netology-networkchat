package Server;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;


public class ConnectedClientsTest {
    ServerThread testThreadMock = Mockito.mock(ServerThread.class);
    ServerThread testThreadMock2 = Mockito.mock(ServerThread.class);
    ConnectedClients testConnectedClients = new ConnectedClients();

    @Test
    public void isCorrectNameTest_OTHER_THREAD(){
        Mockito.when(testThreadMock.getSocketOwner()).thenReturn("TestThread");
        Mockito.when(testThreadMock2.getSocketOwner()).thenReturn("TestThread2");
        testConnectedClients.addServer(testThreadMock);
        Assert.assertTrue(testConnectedClients.isCorrectName("TestThread2", testThreadMock2));
    }

    @Test
    public void isCorrectNameTest_OTHER_THREAD_WITH_SAME_NAME(){
        Mockito.when(testThreadMock.getSocketOwner()).thenReturn("TestThread");
        Mockito.when(testThreadMock2.getSocketOwner()).thenReturn("TestThread2");
        testConnectedClients.addServer(testThreadMock);
        Assert.assertFalse(testConnectedClients.isCorrectName("TestThread", testThreadMock2));
    }


}
