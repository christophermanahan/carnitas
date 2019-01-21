package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPRequestTest {
    @Test
    void itHasAMethod() {
        String method = "GET";
        HTTPRequest request = new HTTPRequest(method);

        String requestMethod = request.method();

        assertEquals(method, requestMethod);
    }

    @Test
    void itHasABody() {
        String method = "GET";
        Optional<String> body = Optional.of("name=<something>");
        HTTPRequest request = new HTTPRequest(method)
          .withBody(body);

        Optional<String> requestBody = request.body();

        assertEquals(body, requestBody);
    }
}
