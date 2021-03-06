package io.github.christophermanahan.carnitas;

import java.io.IOException;
import java.net.ServerSocket;

public class ConnectionListener implements Listener {
    private final ServerSocket serverSocket;

    ConnectionListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public SocketConnection listen() {
        try {
            return new SocketConnection(serverSocket.accept());
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessages.ACCEPT_CONNECTION);
        }
    }
}
