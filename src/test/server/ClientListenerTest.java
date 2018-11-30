package server;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientListenerTest {

  @Test
  void acceptsClientConnection() throws IOException {
    TestServerConnection serverConnection = new TestServerConnection();
    Listener listener = new ClientListener(serverConnection);
    listener.listenForClient();
    assertTrue(serverConnection.accepted);
  }

  private class TestServerConnection extends ServerSocket {

    public boolean accepted;

    TestServerConnection() throws IOException {
      super();
      this.accepted = false;
    }

    public TestClientConnection accept() {
      accepted = true;
      return new TestClientConnection();
    }
  }

  private class TestClientConnection extends Socket {

    public void close() {}

    public boolean isClosed() { return false; }

    public InputStream getInputStream() { return new ByteArrayInputStream("".getBytes()); }

    public OutputStream getOutputStream() { return null; }
  }
}

