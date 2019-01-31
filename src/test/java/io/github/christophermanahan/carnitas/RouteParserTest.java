package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RouteParserTest {
    private int includeEmptyStrings = -1;

    @Test
    void itCanParseARequestStatusLine() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        String uri = "/simple_get";
        String request = method + " " + uri + " " + HTTPResponse.VERSION
          + Serializer.BLANK_LINE;
        RequestBuilder builder = new RequestBuilder();
        Parser<Iterator<String>, Iterator<String>> parser = new RouteParser(builder);

        parser.parse(Arrays.asList(request.split(Serializer.CRLF, includeEmptyStrings)).iterator());

        Route route = builder.get().route();
        assertEquals(new Route(method, uri), route);
    }

    @Test
    void itProvidesTheRestOfTheRequest() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        String uri = "/simple_get";
        String body = "name=<something>";
        String header = Headers.contentLength(body.length());
        String request = method + " " + uri + " " + HTTPResponse.VERSION + Serializer.CRLF
          + header
          + Serializer.BLANK_LINE
          + body;
        Parser<Iterator<String>, Iterator<String>> parser = new RouteParser(new RequestBuilder());

        Iterator<String> unparsed = parser.parse(Arrays.asList(request.split(Serializer.CRLF, includeEmptyStrings)).iterator());

        assertEquals(header, unparsed.next());
    }
}
