package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestParserTest {
    @Test
    void itParsesRequestWithoutBody() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        String uri = "/simple_get";
        String request = method + " " + uri + " " + HTTPResponse.VERSION
          + Serializer.BLANK_LINE;
        Reader reader = new RequestReader(request);
        Parser parser = new RequestParser();

        Optional<HTTPRequest> parsed = parser.parse(reader);

        Optional<HTTPRequest.Method> parsedMethod = parsed.map(HTTPRequest::method);
        Optional<String> parsedUri = parsed.map(HTTPRequest::uri);
        assertEquals(Optional.of(method), parsedMethod);
        assertEquals(Optional.of(uri), parsedUri);
    }

    @Test
    void itParsesRequestWithBody() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        String uri = "/simple_get";
        String body = "name=<something>";
        String request = method + " " + uri + " " + HTTPResponse.VERSION + Serializer.CRLF
          + Headers.contentLength(body.length()) + Serializer.CRLF
          + "Test-Header: Test"
          + Serializer.BLANK_LINE
          + body;
        Reader reader = new RequestReader(request);
        Parser parser = new RequestParser();

        Optional<HTTPRequest> parsed = parser.parse(reader);

        Optional<HTTPRequest.Method> parsedMethod = parsed.map(HTTPRequest::method);
        Optional<String> parsedUri = parsed.map(HTTPRequest::uri);
        Optional<String> parsedBody = parsed.flatMap(HTTPRequest::body);
        assertEquals(Optional.of(method), parsedMethod);
        assertEquals(Optional.of(uri), parsedUri);
        assertEquals(Optional.of(body), parsedBody);
    }

    @Test
    void itParsesRequestStringWithoutBody() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        String uri = "/simple_get";
        String contentLength = Headers.contentLength(0);
        String request = method + " " + uri + " " + HTTPResponse.VERSION + Serializer.CRLF
          + contentLength
          + Serializer.BLANK_LINE;
        Parser<byte[], HTTPRequest> parser = new RequestParser();

        HTTPRequest parsed = parser.parse(request.getBytes());

        HTTPRequest expectedRequest = new RequestBuilder()
          .setMethod(method)
          .setUri(uri)
          .addHeader(contentLength)
          .get();
        assertEquals(expectedRequest, parsed);
    }

    @Test
    void itParsesRequestStringWithBody() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        String uri = "/simple_get";
        String body = "name=<something>";
        String contentLength = Headers.contentLength(body.length());
        String allow = Headers.allow(List.of(method));
        String request = method + " " + uri + " " + HTTPResponse.VERSION + Serializer.CRLF
          + contentLength + Serializer.CRLF
          + allow
          + Serializer.BLANK_LINE
          + body;
        Parser<byte[], HTTPRequest> parser = new RequestParser();

        HTTPRequest parsed = parser.parse(request.getBytes());

        HTTPRequest expectedRequest = new RequestBuilder()
          .setMethod(method)
          .setUri(uri)
          .addHeader(contentLength)
          .addHeader(allow)
          .setBody(Optional.of(body))
          .get();
        assertEquals(expectedRequest, parsed);
    }

    @Test
    void itIsEmptyIfReaderIsEmpty() {
        Reader reader = new Reader() {
            public Optional<String> readUntil(String delimiter) {
                return Optional.empty();
            }

            public Optional<String> read(int numberOfCharacters) {
                return Optional.empty();
            }
        };
        Parser parser = new RequestParser();

        Optional parsed = parser.parse(reader);

        assertEquals(Optional.empty(), parsed);
    }

    private class RequestReader implements Reader {
        private final Iterator<String> request;

        RequestReader(String request) {
            int minimumCharacterLimit = -1;
            this.request = List.of(request.split(Serializer.CRLF, minimumCharacterLimit)).iterator();
        }

        public Optional<String> readUntil(String delimiter) {
            return Optional.of(request.next());
        }

        public Optional<String> read(int numberOfCharacters) {
            return Optional.of(request.next().substring(0, numberOfCharacters));
        }
    }
}
