package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RouterTest {
    @Test
    void itProcessesAGETRequestIntoAResponseIfTheRouteHasBeenAdded() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new ResponseBuilder()
          .setStatus(HTTPResponse.Status.OK)
          .get();
        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.GET, "/simple_get");
        Router router = new Router()
          .get("/simple_get", handler);

        HTTPResponse response = router.handle(request);

        assertTrue(handler.apply(request).equals(response));
    }

    @Test
    void itProcessesAHEADRequestIntoAResponseIfTheRouteHasBeenAdded() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new ResponseBuilder()
          .setStatus(HTTPResponse.Status.OK)
          .get();
        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.HEAD, "/simple_get");
        Router router = new Router()
          .head("/simple_get", handler);

        HTTPResponse response = router.handle(request);

        assertTrue(handler.apply(request).equals(response));
    }

    @Test
    void itProcessesAPOSTRequestIntoAResponseIfTheRouteHasBeenAdded() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new ResponseBuilder()
          .setStatus(HTTPResponse.Status.CREATED)
          .get();
        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.POST, "/simple_post");
        Router router = new Router()
          .post("/simple_post", handler);

        HTTPResponse response = router.handle(request);

        assertTrue(handler.apply(request).equals(response));
    }

    @Test
    void itProcessesARequestIntoAResponseIfMultipleRoutesHaveBeenAdded() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new ResponseBuilder()
          .setStatus(HTTPResponse.Status.OK)
          .get();
        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.GET, "simple_get_again");
        Router router = new Router()
          .get( "/simple_get", handler)
          .get("simple_get_again", handler);

        HTTPResponse response = router.handle(request);

        assertTrue(handler.apply(request).equals(response));
    }

    @Test
    void itProcessesARequestIntoANotFoundResponseIfTheRouteHasNotBeenAdded() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new ResponseBuilder()
          .setStatus(HTTPResponse.Status.OK)
          .get();
        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.POST, "/simple_post");
        Router router = new Router()
          .get( "/simple_get", handler);

        HTTPResponse response = router.handle(request);

        HTTPResponse expectedResponse = new ResponseBuilder()
          .setStatus(HTTPResponse.Status.NOT_FOUND)
          .addHeader(Headers.contentLength(0))
          .get();
        assertTrue(expectedResponse.equals(response));
    }

    @Test
    void itProcessesARequestIntoAMethodNotAllowedResponseIfTheRouteHasBeenAddedToDifferentMethodsThanTheRequestMethod() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new ResponseBuilder()
          .setStatus(HTTPResponse.Status.CREATED)
          .get();
        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.GET, "/simple_post");
        Router router = new Router()
          .post("/simple_post", handler);

        HTTPResponse response = router.handle(request);

        HTTPResponse expectedResponse = new ResponseBuilder()
          .setStatus(HTTPResponse.Status.METHOD_NOT_ALLOWED)
          .addHeader(Headers.contentLength(0))
          .addHeader(Headers.allow(List.of(HTTPRequest.Method.POST)))
          .get();
        assertTrue(expectedResponse.equals(response));
    }
}
