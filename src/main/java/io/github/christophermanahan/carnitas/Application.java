package io.github.christophermanahan.carnitas;

class Application {
    private static final String SIMPLE_GET = "/simple_get";
    private static final String SIMPLE_POST = "/simple_post";

    static Handler router() {
        return new Router()
          .get(SIMPLE_GET, (HTTPRequest request) -> new HTTPResponse(HTTPResponse.Status.OK))
          .head(SIMPLE_GET, (HTTPRequest request) -> new HTTPResponse(HTTPResponse.Status.OK))
          .post(SIMPLE_POST, (HTTPRequest request) -> new HTTPResponse(HTTPResponse.Status.CREATED)
            .withBody(request.body())
          );
    }
}
