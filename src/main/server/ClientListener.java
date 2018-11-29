package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientListener implements Listener {

	private final ServerSocket serverConnection;

	public ClientListener(ServerSocket serverConnection) {
		this.serverConnection = serverConnection;
	}

	public Client listenForClient() throws IOException {
		Socket clientConnection = serverConnection.accept();
		return new ServerClient(clientConnection);
	}
}
