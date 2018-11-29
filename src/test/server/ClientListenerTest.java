package server;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientListenerTest {

	@Test
	void acceptsClientConnection() {
		TestServerConnection serverConnection = new TestServerConnection();
		Listener listener = new ClientListener(serverConnection);
		listener.listenForClient();
		assertTrue(serverConnection.accepted);
	}

	private class TestServerConnection implements ServerConnection {

		public boolean accepted;

		TestServerConnection() {
			this.accepted = false;
		}

		public TestClientConnection accept() {
			accepted = true;
			return new TestClientConnection();
		}
	}

	private class TestClientConnection implements ClientConnection {

		public void close() {}

		public Boolean isClosed() { return null; }

		public InputStream getInputStream() { return new ByteArrayInputStream("".getBytes()); }

		public OutputStream getOutputStream() { return null; }
	}
}

