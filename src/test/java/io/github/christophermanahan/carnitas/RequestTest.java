package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestTest {
    @Test
    void itHasAMethod() {
        Request.Method method = Request.Method.GET;

        Request request = new Request(method, "/simple_get");

        assertEquals(method, request.method());
    }

    @Test
    void itHasAUri() {
        String uri = "/simple_get";

        Request request = new Request(Request.Method.GET, uri);

        assertEquals(uri, request.uri());
    }

    @Test
    void itHasABody() {
        Optional<String> body = Optional.of("name=<something>");

        Request request = new Request(Request.Method.GET, "/simple_get")
          .withBody(body);

        assertEquals(body, request.body());
    }

    @Test
    void itCanProvideAStringfiedRequest() {
        Request.Method method = Request.Method.GET;
        String uri = "/simple_get";
        String body = "name=<something>";
        Request request = new Request(method, uri)
          .withBody(Optional.of(body));

        String expectedStringified = method + " " + uri + Response.CRLF
          + body + Response.CRLF;
        assertEquals(expectedStringified, request.toString());
    }
}
