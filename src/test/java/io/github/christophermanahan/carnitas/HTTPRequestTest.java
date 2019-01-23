package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPRequestTest {
    @Test
    void itHasAMethod() {
        String method = "GET";
        HTTPRequest request = new HTTPRequest(method, "simple_get");

        String requestMethod = request.method();

        assertEquals(method, requestMethod);
    }

    @Test
    void itHasAUri() {
        String uri = "simple_get";
        HTTPRequest request = new HTTPRequest("GET", uri);

        String requestUri = request.uri();

        assertEquals(uri, requestUri);
    }

    @Test
    void itHasABody() {
        Optional<String> body = Optional.of("name=<something>");
        HTTPRequest request = new HTTPRequest("GET", "simple_get")
          .withBody(body);

        Optional<String> requestBody = request.body();

        assertEquals(body, requestBody);
    }
}
