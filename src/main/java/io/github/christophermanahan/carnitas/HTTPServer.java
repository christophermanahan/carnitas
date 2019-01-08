package io.github.christophermanahan.carnitas;

class HTTPServer {
    private final Acceptor acceptor;
    private final Logger errorLogger;

    HTTPServer(Acceptor acceptor, Logger errorLogger) {
        this.acceptor = acceptor;
        this.errorLogger = errorLogger;
    }

    void run() {
        while (acceptor.isAccepting()) {
            try (
              Connection connection = acceptor.accept()
            ) {
                serve(connection);
            } catch (RuntimeException e) {
                errorLogger.log(e.getMessage());
            }
        }
    }

    private void serve(Connection connection) {
        connection.receive()
          .map(request -> new HTTPResponse())
          .ifPresent(connection::send);
    }
}
