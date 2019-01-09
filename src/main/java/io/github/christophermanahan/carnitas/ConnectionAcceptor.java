package io.github.christophermanahan.carnitas;

import java.io.IOException;
import java.net.ServerSocket;

public class ConnectionAcceptor implements Acceptor {
    private final ServerSocket serverSocket;

    ConnectionAcceptor(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public Connection accept() {
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

    public boolean isAccepting() {
        return !serverSocket.isClosed();
    }
}
