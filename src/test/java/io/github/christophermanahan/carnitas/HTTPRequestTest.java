package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
    void itHasARoute() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        String uri = "/simple_get";

        HTTPRequest request = new RequestBuilder()
          .set(method)
          .set(uri)
          .get();

        assertEquals(new Route(method, uri), request.route());
    }

    @Test
    void itHasABody() {
        Optional<String> body = Optional.of("name=<something>");

        HTTPRequest request = new RequestBuilder()
          .set(HTTPRequest.Method.POST)
          .set("/simple_post")
          .set(body)
          .get();


        assertEquals(body, request.body());
    }

    @Test
    void itCanTestEquality() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        String uri = "/simple_get";
        String body = "name=<something>";
        String contentLength = Headers.CONTENT_LENGTH + body.length();
        Headers headers = new Headers().contentLength(body.length());
        HTTPRequest request = new RequestBuilder()
          .set(method)
          .set(uri)
          .set(headers)
          .set(Optional.of(body))
          .get();
        HTTPRequest otherRequest = new RequestBuilder()
          .set(method)
          .set(uri)
          .set(headers)
          .set(Optional.of(body))
          .get();

        assertEquals(request, otherRequest);
    }

    @Test
    void itCanTestInequalityOfMethod() {
        String uri = "/simple_get";
        HTTPRequest request = new RequestBuilder()
          .set(HTTPRequest.Method.GET)
          .set(uri)
          .get();
        HTTPRequest otherRequest = new RequestBuilder()
          .set(HTTPRequest.Method.HEAD)
          .set(uri)
          .get();

        assertNotEquals(request, otherRequest);
    }

    @Test
    void itCanTestInequalityOfUri() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        HTTPRequest request = new RequestBuilder()
          .set(method)
          .set("/simple_get")
          .get();
        HTTPRequest otherRequest = new RequestBuilder()
          .set(method)
          .set("/simple_post")
          .get();

        assertNotEquals(request, otherRequest);
    }

    @Test
    void itCanTestInequalityOfHeaders() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        String uri = "/simple_get";
        HTTPRequest request = new RequestBuilder()
          .set(method)
          .set(uri)
          .set(new Headers()
            .contentLength(0)
          ).get();
        HTTPRequest otherRequest = new RequestBuilder()
          .set(method)
          .set(uri)
          .set(new Headers()
            .contentLength(1)
          ).get();

        assertNotEquals(request, otherRequest);
    }

    @Test
    void itCanTestInequalityOfBody() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        String uri = "/simple_get";
        HTTPRequest request = new RequestBuilder()
          .set(method)
          .set(uri)
          .set(Optional.of("name=<something>"))
          .get();
        HTTPRequest otherRequest = new RequestBuilder()
          .set(method)
          .set(uri)
          .set(Optional.of("name=<something-else>"))
          .get();

        assertNotEquals(request, otherRequest);
    }
}
