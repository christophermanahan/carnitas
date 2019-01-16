package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPRequest2Test {
    @Test
    void itHasAMethod() {
        String method = "GET";
        HTTPRequest2 request = new HTTPRequest2(method);

        String requestMethod = request.method();

        assertEquals(method, requestMethod);
    }

    @Test
    void itHasABody() {
        String method = "GET";
        String body = "name=<something>";
        HTTPRequest2 request = new HTTPRequest2(method)
          .withBody(body);

        String requestBody = request.body();

        assertEquals(body, requestBody);
    }
}