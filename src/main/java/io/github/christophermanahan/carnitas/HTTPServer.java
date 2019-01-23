package io.github.christophermanahan.carnitas;

import java.util.function.Consumer;

class HTTPServer {
    private final Parser parser;
    private final Handler handler;
    private final Logger logger;

    HTTPServer(Parser parser, Handler handler, Logger logger) {
        this.parser = parser;
        this.handler = handler;
        this.logger = logger;
    }

    void start(Listener listener, Consumer<Runnable> context) {
        context.accept(connect(listener));
    }

    private Runnable connect(Listener listener) {
        return () -> {
            try (Connection connection = listener.listen()) {
                serve(connection);
            } catch (RuntimeException e) {
                logger.log(e.getMessage());
            }
        };
    }

    private void serve(Connection connection) {
        parser.parse(new ConnectionReader(connection))
          .map(handler::handle)
          .ifPresent(connection::send);
    }
}
