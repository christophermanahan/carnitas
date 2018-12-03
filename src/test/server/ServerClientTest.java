package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class ServerClientTest {

  private Socket clientConnection;
  private Client serverClient;

  @BeforeEach
  void setup() throws IOException {
    clientConnection = new TestClientConnection();
    serverClient = new ServerClient(clientConnection);
  }

  @Test
  void isNotConnectedIfConnectionIsClosed() throws IOException {
    clientConnection.close();
    assertFalse(new ServerClient(clientConnection).isConnected());
  }

  @Test
  void isConnectedIfConnectionIsOpen() throws IOException {
    assertTrue(new ServerClient(clientConnection).isConnected());
  }

  @Test
  void closeWillCloseConnection() {
    assertFalse(clientConnection.isClosed());
    serverClient.close();
    assertTrue(clientConnection.isClosed());
  }

  @Test
  void readsDataFromConnection() throws IOException {
    assertEquals("echo", serverClient.readFrom().get());
  }

  @Test
  void writesNewLinedDataToConnection() throws IOException {
    serverClient.sendTo("echo");
    assertEquals("echo\n", clientConnection.getOutputStream().toString();
  }

  private class TestClientConnection extends Socket {

    private boolean closed;
    private OutputStream sent;

    TestClientConnection() {
      this.closed = false;
      this.sent = new ByteArrayOutputStream();
    }

    public void close() {
      closed = true;
    }

    public boolean isClosed() { return closed; }

    public InputStream getInputStream() {
      return new ByteArrayInputStream("echo".getBytes());
    }

    public OutputStream getOutputStream() {
      return this.sent;
    }
  }
}
