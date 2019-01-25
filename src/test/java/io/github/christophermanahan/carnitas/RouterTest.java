package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class RouterTest {
    @Test
    void itProcessesAGETRequestIntoAResponseIfTheRouteHasBeenAdded() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new HTTPResponse(HTTPResponse.Status.OK);
        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.GET, "/simple_get");
        Router router = new Router()
          .get("/simple_get", handler);

        HTTPResponse response = router.handle(request);

        assertArrayEquals(handler.apply(request).serialize(), response.serialize());
    }

    @Test
    void itProcessesAHEADRequestIntoAResponseIfTheRouteHasBeenAdded() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new HTTPResponse(HTTPResponse.Status.OK);
        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.HEAD, "/simple_get");
        Router router = new Router()
          .head("/simple_get", handler);

        HTTPResponse response = router.handle(request);

        assertArrayEquals(handler.apply(request).serialize(), response.serialize());
    }

    @Test
    void itProcessesAPOSTRequestIntoAResponseIfTheRouteHasBeenAdded() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new HTTPResponse(HTTPResponse.Status.CREATED);
        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.POST, "/simple_post");
        Router router = new Router()
          .post("/simple_post", handler);

        HTTPResponse response = router.handle(request);

        assertArrayEquals(handler.apply(request).serialize(), response.serialize());
    }

    @Test
    void itProcessesARequestIntoANotFoundResponseIfTheRouteHasNotBeenAdded() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new HTTPResponse(HTTPResponse.Status.OK);
        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.POST, "/simple_post");
        Router router = new Router()
          .get( "/simple_get", handler);

        HTTPResponse response = router.handle(request);

        assertArrayEquals(new HTTPResponse(HTTPResponse.Status.NOT_FOUND).serialize(), response.serialize());
    }

    @Test
    void itProcessesAGETRequestIntoAResponseIfMultipleRoutesHaveBeenAdded() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new HTTPResponse(HTTPResponse.Status.OK);
        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.GET, "simple_get_again");
        Router router = new Router()
          .get( "/simple_get", handler)
          .get("simple_get_again", handler);

        HTTPResponse response = router.handle(request);

        assertArrayEquals(new HTTPResponse(HTTPResponse.Status.OK).serialize(), response.serialize());

    }

    @Test
    void itProcessesAPOSTRequestIntoAResponseIfTheRouteHasBeenAddedToDifferentMethodsThanTheRequestMethod() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new HTTPResponse(HTTPResponse.Status.CREATED);
        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.GET, "/simple_post");
        Router router = new Router()
          .post("/simple_post", handler);

        HTTPResponse response = router.handle(request);

        HTTPResponse expectedResponse = new HTTPResponse(HTTPResponse.Status.METHOD_NOT_ALLOWED)
          .withHeaders(List.of(Headers.allow(List.of(HTTPRequest.Method.POST))));
        assertArrayEquals(expectedResponse.serialize(), response.serialize());
    }
}