package server;

import java.io.*;
import java.util.Optional;

public class ServerClient implements Client {

	private final ClientConnection clientConnection;
	private final BufferedReader reader;
	private final OutputStream writer;

	public ServerClient(ClientConnection clientConnection) {
		this.clientConnection = clientConnection;
		this.reader = setupReader();
		this.writer = setupWriter();
	}

	public Boolean isConnected() {
		return !clientConnection.isClosed();
	}

	public Optional<String> readFrom() {
		try {
			String read = reader.readLine();
			if (read != null) { return Optional.of(read); }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public void sendTo(String data) {
		try {
			writer.write(data.concat("\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		clientConnection.close();
	}

	private BufferedReader setupReader() {
		return new BufferedReader(
			new InputStreamReader(clientConnection.getInputStream())
		);
	}

	private OutputStream setupWriter() { return clientConnection.getOutputStream(); }
}
