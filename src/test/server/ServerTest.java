package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServerTest {

	private TestListener listener;
	private Server server;

	@BeforeEach
	void setup() {
		listener = new TestListener();
		server = new Server(listener);
	}

	@Test
	void serverListensForClientConnection() throws IOException {
		server.run();
		assertTrue(listener.called);
	}

	@Test
	void serverEchosDataWhileConnectionIsOpen() throws IOException {
		server.run();
		List<String> testInput = List.of("echo", "echo");
		listener = new Listener()
		assertEquals(listener.input, listener.output);
	}

	private class TestListener implements Listener {
		Boolean called;
		List<String> output;
		List<String> input;

		TestListener() {
			this.called = false;
			this.output = new ArrayList<>();
			this.input = new ArrayList<>();
		}

		public Client listenForClient() {
			called = true;
			return new TestClient(this.output, this.input);
		}
	}

	private class TestClient implements Client {
		Iterator<String> input;
		List<String> output;

		TestClient(List<String> output, List<String> input) {
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

		public void close() {
		}
	}
}
