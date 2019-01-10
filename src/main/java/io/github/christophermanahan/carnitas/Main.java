package io.github.christophermanahan.carnitas;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    private static Acceptor connectionAcceptor;

    public static void main(String[] args) {
        int port = args.length == 0 ? 33333 : Integer.parseInt(args[0]);
        Logger logger = new ErrorLogger();
        Parser parser = new RequestParser(new HTTPRequestBuilder());
        Handler handler = new RequestHandler(new HTTPResponseBuilder());
        try (
          ServerSocket serverSocket = new ServerSocket(port);
          Acceptor acceptor = new ConnectionAcceptor(serverSocket)
        ) {
            connectionAcceptor = acceptor;
            new HTTPServer(acceptor, parser, handler, logger).run();
        } catch (IOException e) {
            logger.log(e.getMessage());
        }
    }

    public static void stop() {
        connectionAcceptor.close();
    }
}
