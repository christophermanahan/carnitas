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
          .setMethod(method)
          .setUri(uri)
          .get();

        assertEquals(new Route(method, uri), request.route());
    }

    @Test
    void itHasABody() {
        Optional<String> body = Optional.of("name=<something>");

        HTTPRequest request = new RequestBuilder()
          .setMethod(HTTPRequest.Method.POST)
          .setUri("/simple_post")
          .setBody(body)
          .get();


        assertEquals(body, request.body());
    }

    @Test
    void itCanTestEquality() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        String uri = "/simple_get";
        String body = "name=<something>";
        String contentLength = Headers.CONTENT_LENGTH + body.length();
        HTTPRequest request = new RequestBuilder()
          .setMethod(method)
          .setUri(uri)
          .addHeader(contentLength)
          .setBody(Optional.of(body))
          .get();
        HTTPRequest otherRequest = new RequestBuilder()
          .setMethod(method)
          .setUri(uri)
          .addHeader(contentLength)
          .setBody(Optional.of(body))
          .get();

        assertEquals(request, otherRequest);
    }

    @Test
    void itCanTestInequalityOfMethod() {
        String uri = "/simple_get";
        HTTPRequest request = new RequestBuilder()
          .setMethod(HTTPRequest.Method.GET)
          .setUri(uri)
          .get();
        HTTPRequest otherRequest = new RequestBuilder()
          .setMethod(HTTPRequest.Method.HEAD)
          .setUri(uri)
          .get();

        assertNotEquals(request, otherRequest);
    }

    @Test
    void itCanTestInequalityOfUri() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        HTTPRequest request = new RequestBuilder()
          .setMethod(method)
          .setUri("/simple_get")
          .get();
        HTTPRequest otherRequest = new RequestBuilder()
          .setMethod(method)
          .setUri("/simple_post")
          .get();

        assertNotEquals(request, otherRequest);
    }

    @Test
    void itCanTestInequalityOfHeaders() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        String uri = "/simple_get";
        HTTPRequest request = new RequestBuilder()
          .setMethod(method)
          .setUri(uri)
          .addHeader(Headers.CONTENT_LENGTH + 0)
          .get();
        HTTPRequest otherRequest = new RequestBuilder()
          .setMethod(method)
          .setUri(uri)
          .addHeader(Headers.CONTENT_LENGTH + 1)
          .get();

        assertNotEquals(request, otherRequest);
    }

    @Test
    void itCanTestInequalityOfBody() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        String uri = "/simple_get";
        HTTPRequest request = new RequestBuilder()
          .setMethod(method)
          .setUri(uri)
          .setBody(Optional.of("name=<something>"))
          .get();
        HTTPRequest otherRequest = new RequestBuilder()
          .setMethod(method)
          .setUri(uri)
          .setBody(Optional.of("name=<something-else>"))
          .get();

        assertNotEquals(request, otherRequest);
    }
}
