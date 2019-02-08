package io.github.christophermanahan.carnitas;

import java.util.function.Function;

class Application implements Handler {
    private final Handler handler;

    private static final String SIMPLE_GET = "/simple_get";
    private static final String SIMPLE_POST = "/simple_post";
    private static final String REDIRECT = "/redirect";

    Application() {
        this.handler = new LoggingMiddleware(router());
    }

    private Handler router() {
        return new Router()
          .get(SIMPLE_GET, okHandler())
          .get(REDIRECT, redirectHandler())
          .head(SIMPLE_GET, okHandler())
          .post(SIMPLE_POST, createdHandler());
    }

    private Function<Request, Response> okHandler() {
        return (Request request) -> new ResponseBuilder()
          .set(Response.Status.OK)
          .add(Headers.CONTENT_LENGTH + 0)
          .get();
    }

    private Function<Request, Response> createdHandler() {
        return (Request request) -> new ResponseBuilder()
          .set(Response.Status.CREATED)
          .set(request.body())
          .add(Headers.CONTENT_LENGTH + request.body().orElse("").length())
          .get();
    }

    private Function<Request, Response> redirectHandler() {
        return (Request request) -> new ResponseBuilder()
          .set(Response.Status.REDIRECT)
          .add(Headers.LOCATION + SIMPLE_GET)
          .get();
    }

    public Response handle(Request request) {
        return handler.handle(request);
    }
}
