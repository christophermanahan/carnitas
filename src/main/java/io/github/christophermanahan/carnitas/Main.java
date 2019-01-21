package io.github.christophermanahan.carnitas;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Optional;

public class Main {
    private static ServerSocket socket;

    public static void main(String[] args) {
        Logger logger = new ErrorLogger();
        try (
          ServerSocket serverSocket = new ServerSocket(port(args));
        ) {
            socket = serverSocket;
            new HTTPServer(
                    new RequestParser(),
                    new RequestHandler(),
                    logger
            ).start(
                    new ConnectionListener(socket),
                    new WhileOpen(socket)
            );
        } catch (IOException e) {
            logger.log(e.getMessage());
        }
    }

    private static int port(String[] args) {
        return Optional.of(args)
                .filter(a -> a.length > 0)
                .map(b -> b[0])
                .map(Integer::parseInt)
                .orElse(33333);
    }

    public static void stop() {
        try {
            socket.close();
        } catch (IOException ignored) {}
    }
}
