package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class ServerClientTest {

	private ClientConnection clientConnection;
	private Client serverClient;

	@BeforeEach
	void setup() {
		clientConnection = new TestClientConnection();
		serverClient = new ServerClient(clientConnection);
	}

	@Test
	void isNotConnectedIfConnectionIsClosed() {
		clientConnection.close();
		assertFalse(serverClient.isConnected());
	}

	@Test
	void isConnectedIfConnectionIsOpen() {
		assertTrue(serverClient.isConnected());
	}

	@Test
	void closeWillCloseConnection() {
		assertFalse(clientConnection.isClosed());
		serverClient.close();
		assertTrue(clientConnection.isClosed());
	}

	@Test
	void readsDataFromConnection() {
		assertEquals("echo", serverClient.readFrom().get());
	}

	@Test
	void writesNewLinedDataToConnection() {
		serverClient.sendTo("echo");
		assertEquals("echo\n", ((TestClientConnection) clientConnection).sent.toString());
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
