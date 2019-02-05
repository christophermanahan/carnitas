package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RouteTest {
    @Test
    void itHasAMethod() {
        Request.Method method = Request.Method.GET;

        Route route = new Route(method, "/simple_get");

        assertEquals(method, route.method());
    }

    @Test
    void itHasAUri() {
        String uri = "/simple_get";

        Route route = new Route(Request.Method.GET, uri);

        assertEquals(uri, route.uri());
    }

    @Test
    void itCanCheckEquality() {
        Request.Method method = Request.Method.GET;
        String uri = "/simple_get";

        Route route1 = new Route(method, uri);
        Route route = new Route(method, uri);

        assertTrue(route1.equals(route));
    }
}
