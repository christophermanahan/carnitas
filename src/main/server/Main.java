package server;

import java.net.ServerSocket;

public class Main {
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: java EchoServer <port number>");
			System.exit(1);
		}

		int portNumber = Integer.parseInt(args[0]);
		try {
			ServerSocket serverConnection = new ServerSocket(portNumber);
			Listener listener = new ClientListener(serverConnection);
			Server server = new Server(listener);
			server.run();
		} catch (java.io.IOException e) {
			System.out.println("Exception caught when trying to listen on port" + portNumber);
			System.out.println(e.getMessage());
		}
	}
}
