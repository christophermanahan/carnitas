package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EchoServerTest {

  private List<String> incomingData;
  private TestSocket socket;

  @BeforeEach
  void setup() throws IOException {
    incomingData = List.of("data", "data");
    socket = new TestSocket(incomingData);

    new EchoServer(new TestServerSocket(socket)).run();
  }

  @Test
  void dataReceivedFromInputIsSentToOutputUntilDisconnect() throws IOException {
    String outgoingData = socket.getOutputStream().toString();
    List<String> outgoingDataList = List.of(outgoingData.split("\n"));
    assertEquals(incomingData, outgoingDataList);
  }

  @Test void socketIsClosedWhenStreamIsDisconnected() throws IOException {
    assertTrue(socket.closed);
  }

  private class TestServerSocket extends ServerSocket {
    final TestSocket socket;

    TestServerSocket(TestSocket socket) throws IOException {
      this.socket = socket;
    }

    public Socket accept() {
      return socket;
    }
  }

  private class TestSocket extends Socket {
    Iterator<String> incomingData;
    final OutputStream outputStream;
    boolean closed;

    TestSocket(List<String> incomingData) {
      this.incomingData = incomingData.iterator();
      this.outputStream = new ByteArrayOutputStream();
      this.closed = false;
    }

    public boolean isClosed() {
      return closed;
    }

    public void close() {
      disconnectIncomingData();
      closed = true;
    }

    private void disconnectIncomingData() {
      List<String> closedDataStream = List.of();
      incomingData = closedDataStream.iterator();
    }

    public OutputStream getOutputStream() {
      return outputStream;
    }

    public InputStream getInputStream() {
      if (incomingData.hasNext()) {
        return new ByteArrayInputStream(incomingData.next().getBytes());
      } else {
        return new EndOfStream();
      }
    }
  }

  private class EndOfStream extends InputStream {
    public int read() {
      int clientDisconnected = -1;
      return clientDisconnected;
    }
  }
}
