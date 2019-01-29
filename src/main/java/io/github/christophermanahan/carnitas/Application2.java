package io.github.christophermanahan.carnitas;

import java.util.function.Function;

class Application2 implements Handler {
    private final Handler router;

    private static final String SIMPLE_GET = "/simple_get";
    private static final String SIMPLE_POST = "/simple_post";

    Application2() {
        this.router = new Router()
          .get2(SIMPLE_GET, okHandler())
          .head2(SIMPLE_GET, okHandler())
          .post2(SIMPLE_POST, createdHandler());
    }

    private Function<HTTPRequest, HTTPResponse2> okHandler() {
        return (HTTPRequest request) -> new ResponseBuilder()
          .setStatus(HTTPResponse2.Status.OK)
          .addHeader(Headers.contentLength(0))
          .get();
    }

    private Function<HTTPRequest, HTTPResponse2> createdHandler() {
        return (HTTPRequest request) -> new ResponseBuilder()
          .setStatus(HTTPResponse2.Status.CREATED)
          .addHeader(Headers.contentLength(request.body().orElse("").length()))
          .setBody(request.body())
          .get();
    }

    public HTTPResponse handle(HTTPRequest request) {
        return null;
    }

    public HTTPResponse2 handle2(HTTPRequest request) {
        return router.handle2(request);
    }
}
