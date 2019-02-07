package io.github.christophermanahan.carnitas;

public class LoggingMiddleware implements Handler {
    private final MessageLogger logger;
    private final Handler handler;

    LoggingMiddleware(Handler handler) {
        this.handler = handler;
        this.logger = new MessageLogger();
    }

    public Response handle(Request request) {
        logger.log(request.method() + " " + request.uri());
        Response response = handler.handle(request);
        logger.log(response.status().code);
        return response;
    }
}
