package io.github.christophermanahan.carnitas;

public class HTTPServer {

    private final Listener listener;
    private final Parser parser;
    private final Logger errorLogger;
    private Connection connection;

    public HTTPServer(Listener listener, Parser parser, Logger errorLogger) {
        this.listener = listener;
        this.parser = parser;
        this.errorLogger = errorLogger;
    }

    public void run() {
        try {
            connect();
            serveUntilDisconnect();
        } catch (RuntimeException e) {
            errorLogger.log(e.getMessage());
        }
    }

    private void connect() {
        connection = listener.listen();
    }

    private void serveUntilDisconnect() {
        while(connection.isOpen()) {
            serve();
        }
    }

    private void serve() {
        connection.receive()
            .map(parser::parse)
            .map(HTTPResponse::new)
            .ifPresentOrElse(connection::send, connection::close);
    }
}
