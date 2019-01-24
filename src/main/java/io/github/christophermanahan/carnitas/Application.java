package io.github.christophermanahan.carnitas;

class Application implements Handler {
    private final Handler router;

    private static final String SIMPLE_GET = "/simple_get";
    private static final String SIMPLE_POST = "/simple_post";

    Application() {
        this.router = new Router()
          .get(SIMPLE_GET, (HTTPRequest request) -> new HTTPResponse(HTTPResponse.Status.OK))
          .head(SIMPLE_GET, (HTTPRequest request) -> new HTTPResponse(HTTPResponse.Status.OK))
          .post(SIMPLE_POST, (HTTPRequest request) -> new HTTPResponse(HTTPResponse.Status.CREATED)
            .withBody(request.body())
          );
    }

    public HTTPResponse handle(HTTPRequest request) {
        return router.handle(request);
    }
}
