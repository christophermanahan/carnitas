package christophermanahan.github.io.carnitas;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SocketInputTest {

  @Test
  void isEmptyIfSocketRespondsWithoutData() {
    byte[] testData = new byte[0];
    Socket testSocket = new TestSocket(testData);

    assertTrue(new SocketInput(testSocket).receive().isEmpty());
  }

  @Test
  void containsDataIfSocketRespondsWithData() {
    String testString = "data";
    byte[] testData = testString.getBytes();
    Socket testSocket = new TestSocket(testData);

    assertEquals(Optional.of(testString), new SocketInput(testSocket).receive());
  }

  private class TestSocket extends Socket {
    final byte[] testData;

    TestSocket(byte[] testData) {
      this.testData = testData;
    }

    public InputStream getInputStream() throws IOException {
      return new ByteArrayInputStream(testData);
    }
  }
}