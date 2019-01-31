package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

        Route route = new Route(method, uri);
        Route otherRoute = new Route(method, uri);

        assertEquals(route, otherRoute);
    }

    @Test
    void itCanCheckInequalityOfMethod() {
        String uri = "/simple_get";

        Route route = new Route(HTTPRequest.Method.GET, uri);
        Route otherRoute = new Route(HTTPRequest.Method.HEAD, uri);

        assertNotEquals(route, otherRoute);
    }

    @Test
    void itCanCheckInequalityOfUri() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;

        Route route = new Route(method, "/simple_get");
        Route otherRoute = new Route(method, "/simple_post");

        assertNotEquals(route, otherRoute);
    }
}
