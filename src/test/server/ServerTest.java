package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerTest {

	private TestListener listener;
	private Server server;

	@BeforeEach
	void setup() {
		listener = new TestListener();
		server = new Server(listener);
	}

	@Test
	void serverListensForClientConnection() {
		server.run();
		assertTrue(listener.called);
	}

	@Test
	void serverEchosDataWhileConnectionIsOpen() {
		server.run();
		assertEquals(listener.input, listener.output);
	}

	private class TestListener implements Listener {
		Boolean called;
		List<String> output;
		List<String> input;

		TestListener() {
			this.called = false;
			this.output = new ArrayList();
			this.input = List.of("echo", "echo");
		}

		public Client listenForClient() {
			called = true;
			return new TestClient(this.output, this.input);
		}
	}

	private class TestClient implements Client {
		public Iterator<String> input;
		public List<String> output;

		public TestClient(List<String> output, List<String> input) {
			this.input = input.iterator();
			this.output = output;
		}

		public Boolean isConnected() {
			return input.hasNext();
		}

		public Optional<String> readFrom() {
			return Optional.of(input.next());
		}

		public void sendTo(String data) {
			output.add(data);
		}

		public void close() {}
	}
}
