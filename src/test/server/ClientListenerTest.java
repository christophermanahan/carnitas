package server;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientListenerTest {

  @Test
  void acceptsClientConnection() throws IOException {
    TestClientConnection socket = new TestClientConnection("hi");
    TestServerConnection serverConnection = new TestServerConnection(socket);

    Client client = new ClientListener(serverConnection).listenForClient();

    assertEquals(client.readFrom().get(), "hi");
  }

  private class TestServerConnection extends ServerSocket {

    public Socket socket;

    TestServerConnection(Socket socket) throws IOException {
      super();
      this.socket = socket;
    }

    public Socket accept() {
      return socket;
    }
  }

  private class TestClientConnection extends Socket {

    private final String data;

    TestClientConnection(String data) {
      this.data = data;
    }

    public void close() {}

    public boolean isClosed() { return false; }

    public InputStream getInputStream() { return new ByteArrayInputStream(data.getBytes()); }

    public OutputStream getOutputStream() { return null; }
  }
}

