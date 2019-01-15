package io.github.christophermanahan.carnitas;

import java.net.ServerSocket;

class WhileOpen implements RunContext {
    private final ServerSocket serverSocket;

    WhileOpen(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void accept(Runnable runnable) {
        while (open()) {
            runnable.run();
        }
    }

    private boolean open() {
        return !serverSocket.isClosed();
    }
}
