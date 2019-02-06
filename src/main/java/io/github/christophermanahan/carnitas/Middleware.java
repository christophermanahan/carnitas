package io.github.christophermanahan.carnitas;

public class Middleware implements Handler {
    private final MessageLogger logger;
    private final Application application;

    Middleware() {
        this.logger = new MessageLogger();
        this.application = new Application();
    }

    public Response handle(Request request) {
        logger.log(request.toString());
        Response response = application.handle(request);
        logger.log(response.toString());
        return response;
    }
}
