package io.github.christophermanahan.carnitas;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    private static ServerSocket socket;

    public static void main(String[] args) {
        int port = args.length == 0 ? 33333 : Integer.parseInt(args[0]);
        Logger logger = new ErrorLogger();
        Parser2 parser = new RequestParser2();
        Handler2 handler = new RequestHandler2();
        try (
          ServerSocket serverSocket = new ServerSocket(port);
          Listener listener = new ConnectionListener(serverSocket)
        ) {
            socket = serverSocket;
            new HTTPServer2(parser, handler, logger).start(listener, new WhileOpen(socket));
        } catch (IOException e) {
            logger.log(e.getMessage());
        }
    }

    public static void stop() {
        try {
            socket.close();
        } catch (IOException ignored) {}
    }
}
