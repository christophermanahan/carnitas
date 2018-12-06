package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SocketInputTest {

  @Test
  void isEmptyIfSocketRespondsWithEmptyData() {
    String emptyData = "";
    Socket socket = new TestSocket(emptyData);

    assertTrue(new SocketInput(socket).receive().isEmpty());
  }

  @Test
  void containsDataIfSocketRespondsWithData() {
    String incomingData = "data";
    Socket socket = new TestSocket(incomingData);

    assertEquals(Optional.of(incomingData), new SocketInput(socket).receive());
  }

  private class TestSocket extends Socket {
    private final String incomingData;

    TestSocket(String incomingData) {
      this.incomingData = incomingData;
    }

    public InputStream getInputStream() {
      return new ByteArrayInputStream(incomingData.getBytes());
    }
  }
}
