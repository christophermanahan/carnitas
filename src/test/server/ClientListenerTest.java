package server;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientListenerTest {

	@Test
	void acceptsClientConnection() {
		TestServerSocket serverSocket = new TestServerSocket();
		Listener listener = new ClientListener(serverSocket);
		listener.listenForClient();
		assertTrue(serverSocket.accepted);
	}

	private class TestServerSocket implements ServerSocket {

		public boolean accepted;

		TestServerSocket() {
			this.accepted = false;
		}

		public TestClientSocket accept() {
			accepted = true;
			return new TestClientSocket();
		}
	}

	private class TestClientSocket implements ClientSocket {

		public void close() {}

		public Boolean isClosed() { return null; }

		public InputStream getInputStream() { return new ByteArrayInputStream("".getBytes()); }

		public OutputStream getOutputStream() { return null; }
	}
}

