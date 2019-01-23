package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RouteTest {
    @Test
    void itHasAUri() {
        String uri = "/simple_get";

        Route route = new Route(uri, (HTTPRequest r) -> new HTTPResponse(StatusCodes.OK));

        assertEquals(uri, route.uri());
    }

    @Test
    void itHasAHandler() {
        Function<HTTPRequest, HTTPResponse> handler = (HTTPRequest r) -> new HTTPResponse(StatusCodes.OK);

        Route route = new Route("/simple_get", handler);

        assertEquals(handler, route.handler());
    }

}