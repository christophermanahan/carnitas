package server;

public class ClientListener implements Listener {

	private final ServerConnection serverConnection;

	public ClientListener(ServerConnection serverConnection) {
		this.serverConnection = serverConnection;
	}

	public Client listenForClient() {
		ClientConnection clientConnection = serverConnection.accept();
		return new ServerClient(clientConnection);
	}
}
