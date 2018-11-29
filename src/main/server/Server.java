package server;

public class Server {

	private final Listener listener;

	public Server(Listener listener) {
		this.listener = listener;
	}

	public void run() {
		Client client = listener.listenForClient();

		while (client.isConnected()) {
			client.readFrom()
				.ifPresentOrElse(client::sendTo, client::close);
		}
	}
}
