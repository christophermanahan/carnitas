package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.Optional;

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
}
