package server;

public class ClientListener implements Listener {

	private final ServerSocket serverSocket;

	public ClientListener(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public Client listenForClient() {
		ClientSocket clientSocket = serverSocket.accept();
		return new ServerClient(clientSocket);
	}
}
