package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeadersTest {
    @Test
    void itCanConstructAContentLengthHeader() {
        Integer length = 1;

        String header = Headers.contentLength(length);

        assertEquals(Headers.CONTENT_LENGTH + length, header);
    }

    @Test
    void itCanConstructAnAllowHeader() {
        List<HTTPRequest.Method> methods = List.of(HTTPRequest.Method.GET, HTTPRequest.Method.HEAD);

        String header = Headers.allow(methods);

        String expectedHeader = Headers.ALLOW + HTTPRequest.Method.GET + " " + HTTPRequest.Method.HEAD;
        Assertions.assertEquals(expectedHeader, header);
    }
}
