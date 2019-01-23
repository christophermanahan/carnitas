package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RouteTest {
    @Test
    void itHasAUri() {
        String uri = "/simple_get";

        Route route = new Route(uri, (HTTPRequest2 r) -> new HTTPResponse("200 OK"));

        assertEquals(uri, route.uri());
    }

    @Test
    void itHasAHandler() {
        Function<HTTPRequest2, HTTPResponse> handler = (HTTPRequest2 r) -> new HTTPResponse("200 OK");

        Route route = new Route("/simple_get", handler);

        assertEquals(handler, route.handler());
    }

}