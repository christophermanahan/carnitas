package christophermanahan.github.io.carnitas;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

class HTTPServerTest {
  @Test
  void ifNoDataIsReceivedNoDataIsSent() throws IOException {
    List<String> testData = List.of();
    TestServerSocket testServerSocket = new TestServerSocket(testData);
    new HTTPServer(testServerSocket).run();
  }

  private class TestServerSocket extends ServerSocket {
    final List<String> testData;
    final OutputStream sent;

    TestServerSocket(List<String> testData) throws IOException {
      this.testData = testData;
      this.sent = new ByteArrayOutputStream();
    }

    public List<String> getSent() {
      return List.of(sent.toString().split("\n"));
    }

    public Socket accept() {
      return new TestSocket(testData, sent);
    }
  }

  private class TestSocket extends Socket {
    final Iterator<String> testData;
    final OutputStream sent;

    TestSocket(List<String> testData, OutputStream sent) {
      this.testData = testData.iterator();
      this.sent = sent;
    }

    public boolean isClosed() {
      return testData.hasNext();
    }

    public OutputStream getOutputStream() throws IOException {
      return sent;
    }

    public InputStream getInputStream() throws IOException {
      return new ByteArrayInputStream(this.testData.next().getBytes());
    }
  }
}
