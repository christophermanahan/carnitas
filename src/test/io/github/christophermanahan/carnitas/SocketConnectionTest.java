package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SocketConnectionTest {

  @Test
  void isOpenIfSocketIsConnected() {
    boolean connected = true;
    Socket testSocket = new TestSocket(connected);

    assertTrue(new SocketConnection(testSocket).isOpen());
  }

  @Test
  void isClosedIfSocketIsNotConnected() {
    boolean connected = false;
    Socket testSocket = new TestSocket(connected);

    assertFalse(new SocketConnection(testSocket).isOpen());
  }

  @Test
  void closeWillDisconnectConnection() {
    boolean connected = true;
    Socket testSocket = new TestSocket(connected);

    new SocketConnection(testSocket).close();

    assertTrue(testSocket.isClosed());
  }

  private class TestSocket extends Socket {

    boolean connected;

    TestSocket(boolean connected) {
      this.connected = connected;
    }

    public boolean isClosed() {
      return !connected;
    }

    public void close() {
      connected = false;
    }
  }
}