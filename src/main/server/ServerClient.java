package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;

public class ServerClient implements Client {

	private final Socket clientConnection;
	private final BufferedReader reader;
	private final OutputStream writer;

	public ServerClient(Socket clientConnection) throws IOException {
		this.clientConnection = clientConnection;
		this.reader = setupReader();
		this.writer = setupWriter();
	}

	public Boolean isConnected() {
		return !clientConnection.isClosed();
	}

	public Optional<String> readFrom() throws IOException {
		String read = reader.readLine();
		if (read != null) { return Optional.of(read); }
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
		try {
			clientConnection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BufferedReader setupReader() throws IOException {
		return new BufferedReader(
			new InputStreamReader(clientConnection.getInputStream())
		);
	}

	private OutputStream setupWriter() throws IOException { return clientConnection.getOutputStream(); }
}
