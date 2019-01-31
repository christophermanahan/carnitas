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
        String test = "Test-Header: Test";
        String restOfRequest = Headers.CONTENT_LENGTH + contentLength + Serializer.CRLF
          + Headers.ALLOW + HTTPRequest.Method.GET + Serializer.CRLF
          + test
          + Serializer.BLANK_LINE;
        RequestBuilder builder = new RequestBuilder();
        Parser<Iterator<String>, Iterator<String>> parser = new HeadersParser(builder);

        parser.parse(Arrays.asList(restOfRequest.split(Serializer.CRLF, includeEmptyStrings)).iterator());

        Headers expectedHeaders = new Headers()
          .contentLength(contentLength)
          .allow(List.of(method))
          .add(test);
        assertEquals(expectedHeaders, builder.get().headers());
    }

    @Test
    void itProvidesTheRestOfTheRequest() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        int contentLength = 0;
        String test = "Test-Header: Test";
        String body = "name=<something>";
        String restOfRequest = Headers.CONTENT_LENGTH + contentLength + Serializer.CRLF
          + Headers.ALLOW + HTTPRequest.Method.GET + Serializer.CRLF
          + test
          + Serializer.BLANK_LINE
          + body;
        RequestBuilder builder = new RequestBuilder();
        Parser<Iterator<String>, Iterator<String>> parser = new HeadersParser(builder);
        Iterator<String> requestIterator = Arrays.asList(restOfRequest.split(Serializer.CRLF, includeEmptyStrings)).iterator();
        parser.parse(requestIterator);

        assertEquals(body, requestIterator.next());
    }
}
