package io.github.christophermanahan.carnitas;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerSocketListener implements Listener {
    private final ServerSocket serverSocket;

    ServerSocketListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public Connection listen() {
        try {
            return new SocketConnection(serverSocket.accept());
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessages.ACCEPT_CONNECTION);
        }
    }
}
