package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class RequestRouterTest {
    @Test
    void itProcessesAGETRequestIntoAResponseIfTheRouteHasBeenAdded2() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new HTTPResponse(HTTPResponse.StatusCode.OK);
        HTTPRequest request = new HTTPRequest(RequestRouter.GET, "simple_get");
        RequestRouter router = new RequestRouter()
          .get("simple_get", handler);

        HTTPResponse response = router.handle(request);

        assertArrayEquals(handler.apply(request).serialize(), response.serialize());
    }

    @Test
    void itProcessesAHEADRequestIntoAResponseIfTheRouteHasBeenAdded2() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new HTTPResponse(HTTPResponse.StatusCode.OK);
        HTTPRequest request = new HTTPRequest(RequestRouter.HEAD, "simple_get");
        RequestRouter router = new RequestRouter()
          .head("simple_get", handler);

        HTTPResponse response = router.handle(request);

        assertArrayEquals(handler.apply(request).serialize(), response.serialize());
    }

    @Test
    void itProcessesAPOSTRequestIntoAResponseIfTheRouteHasBeenAdded2() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new HTTPResponse(HTTPResponse.StatusCode.CREATED);
        HTTPRequest request = new HTTPRequest(RequestRouter.POST, "simple_post");
        RequestRouter router = new RequestRouter()
          .post("simple_post", handler);

        HTTPResponse response = router.handle(request);

        assertArrayEquals(handler.apply(request).serialize(), response.serialize());
    }

    @Test
    void itProcessesARequestIntoANotFoundResponseIfTheRouteHasNotBeenAdded2() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new HTTPResponse(HTTPResponse.StatusCode.OK);
        HTTPRequest request = new HTTPRequest(RequestRouter.POST, "simple_post");
        RequestRouter router = new RequestRouter()
          .get( "simple_get", handler);

        HTTPResponse response = router.handle(request);

        assertArrayEquals(new HTTPResponse(HTTPResponse.StatusCode.NOT_FOUND).serialize(), response.serialize());
    }

    @Test
    void itProcessesAGETRequestIntoAResponseIfMultipleRoutesHaveBeenAdded2() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest request) -> new HTTPResponse(HTTPResponse.StatusCode.OK);
        HTTPRequest request = new HTTPRequest(RequestRouter.GET, "simple_get_again");
        RequestRouter router = new RequestRouter()
          .get( "simple_get", handler)
          .get("simple_get_again", handler);

        HTTPResponse response = router.handle(request);

        assertArrayEquals(new HTTPResponse(HTTPResponse.StatusCode.OK).serialize(), response.serialize());

    }
}