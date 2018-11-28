package server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServerTest {

	@Test
	void serverListensForClientConnection() {
		TestListener listener = new TestListener();
		Server server = new Server(listener);
		server.run();
		assertTrue(listener.called);
	}

	@Test
	void serverReceivesDataWhileConnectionIsOpen() {
		TestListener listener = new TestListener();
		Server server = new Server(listener);
		server.run();
		assertEquals(listener.output.toString(), "echoechoechoecho");
	}

	@Test
	void serverEchosDataWhileConnectionIsOpen() {
		TestListener listener = new TestListener();
		Server server = new Server(listener);
		server.run();
		assertEquals(listener.input.toString(), "echoechoechoecho");
	}

	private class TestListener implements IListener {
		Boolean called;
		StringBuilder output;
		StringBuilder input;

		TestListener() {
			this.called = false;
			this.output = new StringBuilder();
			this.input = new StringBuilder();
		}

		public IClient listenForClient() {
			called = true;
			return new TestClient(this.output, this.input);
		}
	}

	private class TestClient implements IClient {
		public StringBuilder input;
		public StringBuilder output;

		public TestClient(StringBuilder output, StringBuilder input) {
			this.output = output;
			this.input = input;
		}

		public Boolean connected() {
			if (output.toString().equals("echoechoechoecho")) {
				return false;
			}
			return true;
		}

		public String receiveFrom() {
			String testString = "echo";
			output.append(testString);
			return testString;
		}

		public void sendTo(String data) {
			input.append(data);
		}
	}
}
