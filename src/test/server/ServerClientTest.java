package server;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServerClientTest {

	@Test
	void isNotConnectedIfConnectionIsClosed() {
		ClientConnection testClientConnection = new TestClientConnection();
		Client serverClient = new ServerClient(testClientConnection);
		testClientConnection.close();
		assertFalse(serverClient.isConnected());
	}

	@Test
	void isConnectedIfConnectionIsOpen() {
		ClientConnection testClientConnection = new TestClientConnection();
		Client serverClient = new ServerClient(testClientConnection);
		assertTrue(serverClient.isConnected());
	}

	@Test
	void closeWillCloseConnection() {
		ClientConnection testClientConnection = new TestClientConnection();
		Client serverClient = new ServerClient(testClientConnection);
		assertFalse(testClientConnection.isClosed());
		serverClient.close();
		assertTrue(testClientConnection.isClosed());
	}

	@Test
	void readsDataFromConnection() {
		ClientConnection testClientConnection = new TestClientConnection();
		Client serverClient = new ServerClient(testClientConnection);
		assertEquals("echo", serverClient.readFrom().get());
	}

	@Test
	void writesNewLinedDataToConnection() {
		ClientConnection testClientConnection = new TestClientConnection();
		Client serverClient = new ServerClient(testClientConnection);
		serverClient.sendTo("echo");
		assertEquals("echo\n", ((TestClientConnection) testClientConnection).sent.toString());
	}

	private class TestClientConnection implements ClientConnection {

		private boolean closed;
		public OutputStream sent;

		TestClientConnection() {
			this.closed = false;
			this.sent = new ByteArrayOutputStream();
		}

		public void close() {
			closed = true;
		}

		public Boolean isClosed() { return closed; }

		public InputStream getInputStream() {
			return new ByteArrayInputStream("echo".getBytes());
		}

		public OutputStream getOutputStream() {
			return this.sent;
		}
	}
}
