package io.github.christophermanahan.carnitas;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    private static Acceptor connectionAcceptor;

    public static void main(String[] args) {
        int port = args.length == 0 ? 33333 : Integer.parseInt(args[0]);
        try (
          ServerSocket serverSocket = new ServerSocket(port);
          Acceptor acceptor = new ConnectionAcceptor(serverSocket)
        ) {
            connectionAcceptor = acceptor;
            new HTTPServer(acceptor, new ErrorLogger()).run();
        } catch (IOException ignored) {}
    }

    public static void stop() {
        connectionAcceptor.close();
    }
}
