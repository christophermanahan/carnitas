package server;

public class Server {

	private final IListener listener;

	public Server(IListener listener) {
		this.listener = listener;
	}

	public void run() {
		IClient client = listener.listenForClient();

		while (client.connected()) {
			String data = client.receiveFrom();
			client.sendTo(data);
		}
	}
}
