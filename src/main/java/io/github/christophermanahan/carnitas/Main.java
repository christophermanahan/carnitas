package io.github.christophermanahan.carnitas;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    private static ServerSocket socket;

    public static void main(String[] args) {
        int port = args.length == 0 ? 33333 : Integer.parseInt(args[0]);
        Logger logger = new ErrorLogger();
        Parser parser = new RequestParser();
        Handler handler = new RequestHandler();
        try (
          ServerSocket serverSocket = new ServerSocket(port);
        ) {
            socket = serverSocket;
            new HTTPServer(parser, handler, logger).start(new ConnectionListener(socket), new WhileOpen(socket));
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
