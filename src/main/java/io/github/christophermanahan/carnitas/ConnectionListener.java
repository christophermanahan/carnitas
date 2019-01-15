package io.github.christophermanahan.carnitas;

import java.io.IOException;
import java.net.ServerSocket;

public class ConnectionListener implements Listener {
    private final ServerSocket serverSocket;

    ConnectionListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public Connection listen() {
        try {
            return new SocketConnection(serverSocket.accept());
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessages.ACCEPT_CONNECTION);
        }
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isListening() {
        return !serverSocket.isClosed();
    }
}
