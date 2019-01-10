package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HTTPRequestTest {
    @Test
    void getsRequestMethod() {
        RequestMethod requestMethod = RequestMethod.GET;
        Assertions.assertEquals(requestMethod, new HTTPRequest(requestMethod, null).requestMethod());
    }

    @Test
    void getsBody() {
        String body = "name=<something>";
        Assertions.assertEquals(body, new HTTPRequest(null, body).body());
    }
}
