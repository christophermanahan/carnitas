package Server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

	@Test
	void myFirstTest() {
		Server server = new Server();
		assertEquals(2, 1 + 1);
	}
}
