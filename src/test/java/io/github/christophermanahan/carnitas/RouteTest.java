package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RouteTest {
    @Test
    void itHasAMethod() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;

        Route route = new Route(method, "/simple_get");

        assertEquals(method, route.method());
    }

    @Test
    void itHasAUri() {
        String uri = "/simple_get";

        Route route = new Route(HTTPRequest.Method.GET, uri);

        assertEquals(uri, route.uri());
    }

    @Test
    void itCanCheckEquality() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        String uri = "/simple_get";

        Route route1 = new Route(method, uri);
        Route route = new Route(method, uri);

        assertTrue(route1.equals(route));
    }
}
