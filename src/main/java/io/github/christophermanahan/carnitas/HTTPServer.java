package io.github.christophermanahan.carnitas;

class HTTPServer {
    private final Acceptor acceptor;
    private final Parser parser;
    private final Handler handler;
    private final Logger errorLogger;

    HTTPServer(Acceptor acceptor, Parser parser, Handler handler, Logger errorLogger) {
        this.acceptor = acceptor;
        this.parser = parser;
        this.handler = handler;
        this.errorLogger = errorLogger;
    }

    void run() {
        while (acceptor.isAccepting()) {
            try (Connection connection = acceptor.accept()) {
                serve(connection);
            } catch (RuntimeException e) {
                errorLogger.log(e.getMessage());
            }
        }
    }

    private void serve(Connection connection) {
        parser.parse(connection.receiver())
          .map(handler::handle)
          .ifPresent(connection::send);
    }
}
