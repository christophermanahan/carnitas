package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeadersParserTest {
    private int includeEmptyStrings = -1;

    @Test
    void itCanParseRequestHeaders() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        int contentLength = 0;
        String restOfRequest = Headers.CONTENT_LENGTH + contentLength + Serializer.CRLF
          + Headers.ALLOW + HTTPRequest.Method.GET
          + Serializer.BLANK_LINE;
        RequestBuilder builder = new RequestBuilder();
        Parser<Iterator<String>, Iterator<String>> parser = new HeadersParser(builder);

        parser.parse(Arrays.asList(restOfRequest.split(Serializer.CRLF, includeEmptyStrings)).iterator());

        Headers expectedHeaders = new Headers()
          .contentLength(contentLength)
          .allow(List.of(method));
        assertEquals(expectedHeaders, builder.get().headers());
    }
}
