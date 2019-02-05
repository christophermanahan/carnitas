package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RouterTest {
    @Test
    void itProcessesAGETRequestIntoAResponseIfTheRouteHasBeenAdded() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new ResponseBuilder()
          .set(HTTPResponse.Status.OK)
          .get();
        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.GET, "/simple_get");
        Router router = new Router()
          .get("/simple_get", handler);

        HTTPResponse response = router.handle(request);

        assertEquals(handler.apply(request), response);
    }

    @Test
    void itProcessesAHEADRequestIntoAResponseIfTheRouteHasBeenAdded() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new ResponseBuilder()
          .set(HTTPResponse.Status.OK)
          .get();
        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.HEAD, "/simple_get");
        Router router = new Router()
          .head("/simple_get", handler);

        HTTPResponse response = router.handle(request);

        assertEquals(handler.apply(request), response);
    }

    @Test
    void itProcessesAPOSTRequestIntoAResponseIfTheRouteHasBeenAdded() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new ResponseBuilder()
          .set(HTTPResponse.Status.CREATED)
          .get();
        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.POST, "/simple_post");
        Router router = new Router()
          .post("/simple_post", handler);

        HTTPResponse response = router.handle(request);

        assertEquals(handler.apply(request), response);
    }

    @Test
    void itProcessesARequestIntoAResponseIfMultipleRoutesHaveBeenAdded() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new ResponseBuilder()
          .set(HTTPResponse.Status.OK)
          .get();
        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.GET, "simple_get_again");
        Router router = new Router()
          .get( "/simple_get", handler)
          .get("simple_get_again", handler);

        HTTPResponse response = router.handle(request);

        assertEquals(handler.apply(request), response);
    }

    @Test
    void itProcessesARequestIntoANotFoundResponseIfTheRouteHasNotBeenAdded() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new ResponseBuilder()
          .set(HTTPResponse.Status.OK)
          .get();
        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.POST, "/simple_post");
        Router router = new Router()
          .get( "/simple_get", handler);

        HTTPResponse response = router.handle(request);

        HTTPResponse expectedResponse = new ResponseBuilder()
          .set(HTTPResponse.Status.NOT_FOUND)
          .add(Headers.CONTENT_LENGTH + 0)
          .get();
        assertEquals(expectedResponse, response);
    }

    @Test
    void itDoesNotAllowRequestsToRoutesThatCannotRespondToTheRequestMethod() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new ResponseBuilder()
          .set(HTTPResponse.Status.CREATED)
          .get();
        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.GET, "/simple_post");
        Router router = new Router()
          .post("/simple_post", handler);

        HTTPResponse response = router.handle(request);

        HTTPResponse expectedResponse = new ResponseBuilder()
          .set(HTTPResponse.Status.METHOD_NOT_ALLOWED)
          .add(Headers.ALLOW + HTTPRequest.Method.OPTIONS + " " + HTTPRequest.Method.POST)
          .add(Headers.CONTENT_LENGTH + 0)
          .get();
        assertEquals(expectedResponse, response);
    }

    @Test
    void itProcessesAnOPTIONSRequestIntoAResponse() {
        HTTPResponse.Status status = HTTPResponse.Status.OK;
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new ResponseBuilder()
          .set(status)
          .get();
        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.OPTIONS, "/simple_get");
        Router router = new Router()
          .get( "/simple_get", handler);

        HTTPResponse response = router.handle(request);


        HTTPResponse expectedResponse = new ResponseBuilder()
          .set(HTTPResponse.Status.OK)
          .add(Headers.CONTENT_LENGTH + 0)
          .add(Headers.ALLOW + HTTPRequest.Method.GET + " " + HTTPRequest.Method.OPTIONS)
          .get();
        assertEquals(expectedResponse, response);
    }
}
