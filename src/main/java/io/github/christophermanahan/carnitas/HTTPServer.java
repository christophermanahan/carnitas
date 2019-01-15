package io.github.christophermanahan.carnitas;

class HTTPServer {
    private final Listener listener;
    private final Parser parser;
    private final Handler handler;
    private final Logger errorLogger;

    HTTPServer(Listener listener, Parser parser, Handler handler, Logger errorLogger) {
        this.listener = listener;
        this.parser = parser;
        this.handler = handler;
        this.errorLogger = errorLogger;
    }

    void run() {
        while (listener.isListening()) {
            try (Connection connection = listener.listen()) {
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
