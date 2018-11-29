package server;

public class Server {

	private final Listener listener;

	public Server(Listener listener) {
		this.listener = listener;
	}

	public void run() {
		Client serverClient = listener.listenForClient();

		while (serverClient.isConnected()) {
			serverClient.readFrom()
				.ifPresentOrElse(serverClient::sendTo, serverClient::close);
		}
	}
}
