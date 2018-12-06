package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

class SocketOutputTest {

  @Test
  void sendsDataAppendedWithNewLineToSocketOutput() throws IOException {
    String testString = "data";
    String result = testString + "\n";
    Socket testSocket = new TestSocket();
    Output socketOutput = new SocketOutput(testSocket);

    socketOutput.send(testString);

    Assertions.assertEquals(result, testSocket.getOutputStream().toString());
  }

  private class TestSocket extends Socket {

    final ByteArrayOutputStream sent;

    TestSocket() {
      this.sent = new ByteArrayOutputStream();
    }

    public OutputStream getOutputStream() throws IOException {
      return sent;
    }
  }
}