package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPRequestTest {
    @Test
    void itHasAMethod() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;

        HTTPRequest request = new HTTPRequest(method, "/simple_get");

        assertEquals(method, request.method());
    }

    @Test
    void itHasAUri() {
        String uri = "/simple_get";

        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.GET, uri);

        assertEquals(uri, request.uri());
    }

    @Test
    void itHasABody() {
        Optional<String> body = Optional.of("name=<something>");

        HTTPRequest request = new HTTPRequest(HTTPRequest.Method.GET, "/simple_get")
          .withBody(body);

        assertEquals(body, request.body());
    }

    @Test
    void itCanTestEquality() {
        Route route = new Route(HTTPRequest.Method.GET, "/simple_get");
        String body = "name=<something>";
        String contentLength = Headers.contentLength(body.length());
        HTTPRequest request = new RequestBuilder()
          .setRoute(route)
          .addHeader(contentLength)
          .setBody(Optional.of(body))
          .get();
        HTTPRequest otherRequest = new RequestBuilder()
          .setRoute(route)
          .addHeader(contentLength)
          .setBody(Optional.of(body))
          .get();

        assertEquals(request, otherRequest);
    }

    @Test
    void itCanTestInequalityOfRoute() {
        Route route = new Route(HTTPRequest.Method.GET, "/simple_get");
        Route otherRoute = new Route(HTTPRequest.Method.HEAD, "/simple_get");
        String body = "name=<something>";
        String contentLength = Headers.contentLength(body.length());
        HTTPRequest request = new RequestBuilder()
          .setRoute(route)
          .addHeader(contentLength)
          .setBody(Optional.of(body))
          .get();
        HTTPRequest otherRequest = new RequestBuilder()
          .setRoute(otherRoute)
          .addHeader(contentLength)
          .setBody(Optional.of(body))
          .get();

        assertNotEquals(request, otherRequest);
    }

    @Test
    void itCanTestInequalityOfHeaders() {
        Route route = new Route(HTTPRequest.Method.GET, "/simple_get");
        HTTPRequest request = new RequestBuilder()
          .setRoute(route)
          .addHeader(Headers.contentLength(0))
          .get();
        HTTPRequest otherRequest = new RequestBuilder()
          .setRoute(route)
          .addHeader(Headers.contentLength(1))
          .get();

        assertNotEquals(request, otherRequest);
    }

    @Test
    void itCanTestInequalityOfBody() {
        Route route = new Route(HTTPRequest.Method.GET, "/simple_get");
        HTTPRequest request = new RequestBuilder()
          .setRoute(route)
          .setBody(Optional.of("name=<something>"))
          .get();
        HTTPRequest otherRequest = new RequestBuilder()
          .setRoute(route)
          .setBody(Optional.of("name=<something-else>"))
          .get();

        assertNotEquals(request, otherRequest);
    }
}
