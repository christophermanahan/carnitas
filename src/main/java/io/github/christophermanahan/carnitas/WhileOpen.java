package io.github.christophermanahan.carnitas;

import java.net.ServerSocket;
import java.util.function.Consumer;

class WhileOpen implements Consumer<Runnable> {
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
