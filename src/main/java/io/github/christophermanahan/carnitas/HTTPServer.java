package io.github.christophermanahan.carnitas;

class HTTPServer {
    private final Listener listener;
    private final Logger errorLogger;
    private Connection connection;

    HTTPServer(Listener listener, Logger errorLogger) {
        this.listener = listener;
        this.errorLogger = errorLogger;
    }

    void run() {
        try {
            connect();
            serve();
            close();
        } catch (RuntimeException e) {
            errorLogger.log(e.getMessage());
        }
    }

    private void connect() {
        connection = listener.listen();
    }

    private void serve() {
        connection.receive()
            .map(request -> new HTTPResponse())
            .ifPresent(connection::send);
    }

    private void close() {
        connection.close();
    }
}
