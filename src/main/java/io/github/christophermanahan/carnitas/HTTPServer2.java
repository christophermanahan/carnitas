package io.github.christophermanahan.carnitas;

import java.util.function.Consumer;

class HTTPServer2 {
    private final Parser2 parser;
    private final Handler2 handler;
    private final Logger logger;

    HTTPServer2(Parser2 parser, Handler2 handler, Logger logger) {
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
