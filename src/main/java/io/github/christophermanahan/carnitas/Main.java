package io.github.christophermanahan.carnitas;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(port(args))) {
            new HTTPServer(
              new RequestParser(),
              new Middleware(),
              new MessageLogger()
            ).start(
              new ConnectionListener(serverSocket),
              new WhileOpen(serverSocket)
            );
        } catch (IOException e) {
            new MessageLogger().log(e.getMessage());
        }
    }

    private static int port(String[] args) {
        return Optional.of(args)
          .filter(a -> a.length > 0)
          .map(b -> b[0])
          .map(Integer::parseInt)
          .orElse(33333);
    }
}
