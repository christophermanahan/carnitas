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

import static org.junit.jupiter.api.Assertions.*;

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

    assertEquals(Optional.of(data), received);
  }

  @Test
  void sendsNewLineAppendedDataToSocket() throws IOException {
    Socket socket = new TestSocket(null);

    new SocketConnection(socket).send(data);

    assertEquals(data.concat("\n"), socket.getOutputStream().toString());
  }

  @Test
  void throwsExceptionIfSendFails() {
    Socket socket = new OutputStreamException();
    Connection connection = new SocketConnection(socket);

    RuntimeException e = assertThrows(RuntimeException.class, ()->{ connection.send(data); });

    Assertions.assertEquals(ErrorMessages.SEND_TO_CONNECTION, e.getMessage());
  }

  @Test
  void closeClosesSocketConnection() {
    Socket socket = new TestSocket(null);

    new SocketConnection(socket).close();

    assertTrue(socket.isClosed());
  }

  @Test
  void throwsExceptionIfCloseFails() {
    Socket socket = new CloseException();
    Connection connection = new SocketConnection(socket);

    RuntimeException e = assertThrows(RuntimeException.class, connection::close);

    Assertions.assertEquals(ErrorMessages.CLOSE_CONNECTION, e.getMessage());
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
