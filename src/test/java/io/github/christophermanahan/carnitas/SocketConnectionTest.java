package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;

class SocketConnectionTest {

  String data;

  @BeforeEach
  void setup() {
    data = "data";
  }

  @Test
  void receivesDataFromSocket() {
    Socket socket = new TestSocket(data);

    Optional<String> received = new SocketConnection(socket).receive();

    Assertions.assertEquals(Optional.of(data), received);
  }

  @Test
  void sendsNewLineAppendedDataToSocket() throws IOException {
    Socket socket = new TestSocket(null);

    new SocketConnection(socket).send(data);

    Assertions.assertEquals(data.concat("\n"), socket.getOutputStream().toString());
  }

  @Test
  void throwsExceptionIfSendFails() {
    Socket socket = new OutputStreamException();

    Connection connection = new SocketConnection(socket);

    Assertions.assertThrows(SendToConnectionException.class, ()->{ connection.send(data); });
  }

  @Test
  void closeClosesSocketConnection() {
    Socket socket = new TestSocket(null);

    new SocketConnection(socket).close();

    Assertions.assertTrue(socket.isClosed());
  }

  @Test
  void throwsExceptionIfCloseFails() {
    Socket socket = new CloseException();

    Connection connection = new SocketConnection(socket);

    Assertions.assertThrows(ConnectionCloseException.class, connection::close);
  }

  private class TestSocket extends Socket {

    private final String receive;
    private OutputStream output;
    private boolean closed;

    public TestSocket(String receive) {
      this.receive = receive;
      this.output = new ByteArrayOutputStream();
      this.closed = false;
    }

    public InputStream getInputStream() {
      return new ByteArrayInputStream(receive.getBytes());
    }

    public OutputStream getOutputStream() {
      return output;
    }

    public synchronized void close() {
      closed = true;
    }

    public boolean isClosed() {
      return closed;
    }
  }

  private class OutputStreamException extends Socket {

    public OutputStream getOutputStream() throws IOException {
      throw new IOException();
    }
  }

  private class CloseException extends Socket {

    public synchronized void close() throws IOException {
      throw new IOException();
    }
  }
}