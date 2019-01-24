package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class RouterTest {
    @Test
    void itProcessesAGETRequestIntoAResponseIfTheRouteHasBeenAdded() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new HTTPResponse(HTTPResponse.StatusCode.OK);
        HTTPRequest request = new HTTPRequest(Router.GET, "/simple_get");
        Router router = new Router()
          .get("/simple_get", handler);

        HTTPResponse response = router.handle(request);

        assertArrayEquals(handler.apply(request).serialize(), response.serialize());
    }

    @Test
    void itProcessesAHEADRequestIntoAResponseIfTheRouteHasBeenAdded() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new HTTPResponse(HTTPResponse.StatusCode.OK);
        HTTPRequest request = new HTTPRequest(Router.HEAD, "/simple_get");
        Router router = new Router()
          .head("/simple_get", handler);

        HTTPResponse response = router.handle(request);

        assertArrayEquals(handler.apply(request).serialize(), response.serialize());
    }

    @Test
    void itProcessesAPOSTRequestIntoAResponseIfTheRouteHasBeenAdded() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new HTTPResponse(HTTPResponse.StatusCode.CREATED);
        HTTPRequest request = new HTTPRequest(Router.POST, "/simple_post");
        Router router = new Router()
          .post("/simple_post", handler);

        HTTPResponse response = router.handle(request);

        assertArrayEquals(handler.apply(request).serialize(), response.serialize());
    }

    @Test
    void itProcessesARequestIntoANotFoundResponseIfTheRouteHasNotBeenAdded() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new HTTPResponse(HTTPResponse.StatusCode.OK);
        HTTPRequest request = new HTTPRequest(Router.POST, "/simple_post");
        Router router = new Router()
          .get( "/simple_get", handler);

        HTTPResponse response = router.handle(request);

        assertArrayEquals(new HTTPResponse(HTTPResponse.StatusCode.NOT_FOUND).serialize(), response.serialize());
    }

    @Test
    void itProcessesAGETRequestIntoAResponseIfMultipleRoutesHaveBeenAdded() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new HTTPResponse(HTTPResponse.StatusCode.OK);
        HTTPRequest request = new HTTPRequest(Router.GET, "simple_get_again");
        Router router = new Router()
          .get( "/simple_get", handler)
          .get("simple_get_again", handler);

        HTTPResponse response = router.handle(request);

        assertArrayEquals(new HTTPResponse(HTTPResponse.StatusCode.OK).serialize(), response.serialize());

    }
}