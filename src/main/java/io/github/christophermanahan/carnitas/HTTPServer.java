package io.github.christophermanahan.carnitas;

public class HTTPServer {

    private final Listener listener;
    private final Logger errorLogger;
    private Connection connection;

    public HTTPServer(Listener listener, Logger errorLogger) {
        this.listener = listener;
        this.errorLogger = errorLogger;
    }

    public void run() {
        try {
            connect();
            serveWhileOpen();
        } catch (RuntimeException e) {
            errorLogger.log(e.getMessage());
        }
    }

    private void connect() {
        connection = listener.listen();
    }

    private void serveWhileOpen() {
        while (connection.isOpen()) {
            serve();
        }
    }

    private void serve() {
        connection.receive()
            .map(request -> new HTTPResponse())
            .ifPresentOrElse(connection::send, connection::close);
    }
}
