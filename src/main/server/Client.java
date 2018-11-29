package server;

import java.io.IOException;
import java.util.Optional;

public interface Client {
	Boolean isConnected();

	Optional<String> readFrom() throws IOException;

	void sendTo(String data);

	void close();
}
