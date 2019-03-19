package io.github.christophermanahan.carnitas;

import java.util.Optional;
import java.util.function.Function;

class Application implements Handler {
    private final Handler handler;

    private static final String SIMPLE_GET = "/simple_get";
    private static final String SIMPLE_POST = "/simple_post";
    private static final String REDIRECT = "/redirect";
    private static final String REDIRECTED = "/redirected";

    Application() {
        this.handler = new LoggingMiddleware(router());
    }

    private Handler router() {
        return new Router()
          .get(SIMPLE_GET, okHandler())
          .get(REDIRECT, redirectHandler())
          .get(REDIRECTED, redirectedHandler())
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
          .add(Headers.LOCATION + REDIRECTED)
          .get();
    }

    private Function<Request, Response> redirectedHandler() {
        String body = "<html><head/><body><p>You have been redirected</p></body></html>";
        return (Request request) -> new ResponseBuilder()
          .set(Response.Status.OK)
          .add("Content-Type: text/html")
          .add(Headers.CONTENT_LENGTH + body.length())
          .set(Optional.of(body))
          .get();
    }

    public Response handle(Request request) {
        return handler.handle(request);
    }
}
