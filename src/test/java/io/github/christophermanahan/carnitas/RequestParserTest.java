package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestParserTest {
    @Test
    void itParsesRequestWithoutBody() {
        String method = "GET";
        String uri = "/simple_get";
        String body = "name=<something>";
        String request = method + " " + uri + " " + HTTPResponse.VERSION
          + HTTPResponse.BLANK_LINE;
        Reader reader = new RequestReader(request);
        Parser parser = new RequestParser();

        Optional<HTTPRequest> parsed = parser.parse(reader);

        Optional<String> parsedMethod = parsed.map(HTTPRequest::method);
        Optional<String> parsedUri = parsed.map(HTTPRequest::uri);
        assertEquals(Optional.of(method), parsedMethod);
        assertEquals(Optional.of(uri), parsedUri);
    }

    @Test
    void itParsesRequestWithBody() {
        String method = "GET";
        String uri = "/simple_get";
        String body = "name=<something>";
        String request = method + " " + uri + " " + HTTPResponse.VERSION + HTTPResponse.CRLF
          + Headers.contentLength(body.length()) + HTTPResponse.CRLF
          + "Test-Header: Test"
          + HTTPResponse.BLANK_LINE
          + body;
        Reader reader = new RequestReader(request);
        Parser parser = new RequestParser();

        Optional<HTTPRequest> parsed = parser.parse(reader);

        Optional<String> parsedMethod = parsed.map(HTTPRequest::method);
        Optional<String> parsedUri = parsed.map(HTTPRequest::uri);
        Optional<String> parsedBody = parsed.flatMap(HTTPRequest::body);
        assertEquals(Optional.of(method), parsedMethod);
        assertEquals(Optional.of(uri), parsedUri);
        assertEquals(Optional.of(body), parsedBody);
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

        Optional<HTTPRequest> parsed = parser.parse(reader);

        assertEquals(Optional.empty(), parsed);
    }

    private class RequestReader implements Reader {
        private final Iterator<String> request;

        RequestReader(String request) {
            this.request = List.of(request.split(HTTPResponse.CRLF, -1)).iterator();
        }

        public Optional<String> readUntil(String delimiter) {
            return Optional.of(request.next());
        }

        public Optional<String> read(int numberOfCharacters) {
            return Optional.of(request.next().substring(0, numberOfCharacters));
        }
    }
}
