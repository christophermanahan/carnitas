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
		ClientSocket testSocket = new TestSocket();
		Client serverClient = new ServerClient(testSocket);
		testSocket.close();
		assertFalse(serverClient.isConnected());
	}

	@Test
	void isConnectedIfConnectionIsOpen() {
		ClientSocket testSocket = new TestSocket();
		Client serverClient = new ServerClient(testSocket);
		assertTrue(serverClient.isConnected());
	}

	@Test
	void closeWillCloseConnection() {
		ClientSocket testSocket = new TestSocket();
		Client serverClient = new ServerClient(testSocket);
		assertFalse(testSocket.isClosed());
		serverClient.close();
		assertTrue(testSocket.isClosed());
	}

	@Test
	void readsDataFromConnection() {
		ClientSocket testSocket = new TestSocket();
		Client serverClient = new ServerClient(testSocket);
		assertEquals("echo", serverClient.readFrom().get());
	}

	@Test
	void writesNewLineDataToConnection() {
		ClientSocket testSocket = new TestSocket();
		Client serverClient = new ServerClient(testSocket);
		serverClient.sendTo("echo");
		assertEquals("echo\n", ((TestSocket) testSocket).sent.toString());
	}

	private class TestSocket implements ClientSocket {

		private boolean closed;
		public OutputStream sent;

		TestSocket() {
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
