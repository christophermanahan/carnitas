package io.github.christophermanahan.carnitas;

public class LoggingMiddleware implements Handler {
    private final MessageLogger logger;
    private final Handler handler;

    LoggingMiddleware(Handler handler) {
        this.logger = new MessageLogger();
        this.handler = handler;
    }

    public Response handle(Request request) {
        logger.log(request.toString());
        Response response = handler.handle(request);
        logger.log(response.toString());
        return response;
    }
}
