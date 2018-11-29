package server;

import java.io.*;
import java.util.Optional;

public class ServerClient implements Client {

	private final ClientSocket clientSocket;
	private final BufferedReader reader;
	private final OutputStream writer;

	public ServerClient(ClientSocket clientSocket) {
		this.clientSocket = clientSocket;
		this.reader = setupReader();
		this.writer = setupWriter();
	}

	public Boolean isConnected() {
		return !clientSocket.isClosed();
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
		clientSocket.close();
	}

	private BufferedReader setupReader() {
		return new BufferedReader(
			new InputStreamReader(clientSocket.getInputStream())
		);
	}

	private OutputStream setupWriter() { return clientSocket.getOutputStream(); }
}
