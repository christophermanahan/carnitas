package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPRequest2Test {
    @Test
    void itHasAMethod() {
        String method = "GET";
        HTTPRequest2 request = new HTTPRequest2(method, "simple_get");

        String requestMethod = request.method();

        assertEquals(method, requestMethod);
    }

    @Test
    void itHasAUri() {
        String uri = "simple_get";
        HTTPRequest2 request = new HTTPRequest2("GET", uri);

        String requestUri = request.uri();

        assertEquals(uri, requestUri);
    }

    @Test
    void itHasABody() {
        Optional<String> body = Optional.of("name=<something>");
        HTTPRequest2 request = new HTTPRequest2("GET", "simple_get")
          .withBody(body);

        Optional<String> requestBody = request.body();

        assertEquals(body, requestBody);
    }
}
