package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeadersTest {
    @Test
    void itCanConstructAContentLengthHeader() {
        Integer length = 1;

        String header = Headers.contentLength(length);

        assertEquals(Headers.CONTENT_LENGTH + length, header);
    }
}
