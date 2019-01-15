package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HTTPRequest2Test {
    @Test
    void itHasAMethod() {
        String method = "GET";
        HTTPRequest2 request = new HTTPRequest2(method);

        String requestMethod = request.method();

        Assertions.assertEquals(method, requestMethod);
    }
}