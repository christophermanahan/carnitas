package io.github.christophermanahan.carnitas;

import java.util.List;
import java.util.function.Function;

class Application implements Handler {
    private final Handler router;

    private static final String SIMPLE_GET = "/simple_get";
    private static final String SIMPLE_POST = "/simple_post";

    Application() {
        this.router = new Router()
          .get(SIMPLE_GET, okHandler())
          .head(SIMPLE_GET, okHandler())
          .post(SIMPLE_POST, createdHandler());
    }

    private Function<HTTPRequest, HTTPResponse> okHandler() {
        return (HTTPRequest request) -> new ResponseBuilder()
          .set(HTTPResponse.Status.OK)
          .set(List.of(
            Headers.contentLength(0)
          )).get();
    }

    private Function<HTTPRequest, HTTPResponse> createdHandler() {
        return (HTTPRequest request) -> new ResponseBuilder()
          .set(HTTPResponse.Status.CREATED)
          .set(request.body())
          .set(List.of(
            Headers.contentLength(request.body().orElse("").length()))
          ).get();
    }

    public HTTPResponse handle(HTTPRequest request) {
        return router.handle(request);
    }
}
