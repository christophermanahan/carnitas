package server;

import java.util.Optional;

public interface Client {
	Boolean isConnected();

	Optional<String> readFrom();

	void sendTo(String data);

	void close();
}
