package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServerSocketListenerTest {

  @Test
  void listensForAConnection() throws IOException {
    String data = "data";
    ServerSocket serverSocket = new TestServerSocket(data);

    Connection connection = new ServerSocketListener(serverSocket).listen();

    assertEquals(Optional.of(data), connection.receive());
  }

  @Test
  void throwsExceptionIfConnectionAcceptionFails() throws IOException {
    ServerSocket serverSocket = new ConnectionException();
    Listener listener = new ServerSocketListener(serverSocket);

    RuntimeException e = assertThrows(RuntimeException.class, listener::listen);

    Assertions.assertEquals(ErrorMessages.ACCEPT_CONNECTION, e.getMessage());
  }

  private class TestServerSocket extends ServerSocket {
    private final String data;

    public TestServerSocket(String data) throws IOException {
      this.data = data;
    }

    public Socket accept() {
      return new TestSocket(data);
    }
  }

  private class ConnectionException extends ServerSocket {
    public ConnectionException() throws IOException {
    }

    public Socket accept() throws IOException {
      throw new IOException();
    }
  }

  private class TestSocket extends Socket {
    private final String data;

    public TestSocket(String data) {
      this.data = data;
    }

    public InputStream getInputStream() {
      return new ByteArrayInputStream(data.getBytes());
    }
  }
}
