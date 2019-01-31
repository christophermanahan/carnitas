package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeadersTest {
    @Test
    void itCanConstructAContentLengthHeader() {
        Integer length = 1;

        Headers headers = new Headers()
          .contentLength(length);

        HashSet<String> expectedHeaders = new HashSet<>(List.of(Headers.CONTENT_LENGTH + length));
        assertEquals(expectedHeaders, headers.get());
    }

    @Test
    void itCanConstructAnAllowHeader() {
        List<HTTPRequest.Method> methods = List.of(HTTPRequest.Method.GET, HTTPRequest.Method.HEAD);

        Headers headers = new Headers()
          .allow(methods);

        HashSet<String> expectedHeaders = new HashSet<>(List.of(Headers.ALLOW + HTTPRequest.Method.GET + " " + HTTPRequest.Method.HEAD));
        Assertions.assertEquals(expectedHeaders, headers.get());
    }

    @Test
    void itCanAddArbitraryHeaders() {
        String testA = "Test-Header: TestA";
        String testB = "Test-Header: TestB";
        Headers headers = new Headers()
          .add(testA)
          .add(testB);

        HashSet<String> expectedHeaders = new HashSet<>(List.of(testA, testB));
        assertEquals(expectedHeaders, headers.get());
    }

    @Test
    void itCanTestEquality() {
        Integer length = 1;
        Headers headers = new Headers()
          .contentLength(length);
        Headers otherHeaders = new Headers()
          .contentLength(length);

        assertEquals(headers, otherHeaders);
    }
}
