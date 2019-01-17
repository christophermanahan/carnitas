package io.github.christophermanahan.carnitas;

import java.util.List;
import java.util.Optional;

public class RequestParser implements Parser {
    public Optional<HTTPRequest> parse(Reader reader) {
        Optional<String> method = getMethod(reader);
        Optional<Integer> contentLength = parseHeadersForContentLength(reader);
        Optional<String> body = getBody(reader, contentLength);
        return request(method, body);
    }

    private Optional<String> readLine(Reader reader) {
        return reader.readUntil(HTTPResponse.CRLF);
    }

    private Optional<String> getMethod(Reader reader) {
        return readLine(reader)
          .map(this::extractMethod);
    }

    private String extractMethod(String statusLine) {
        return List.of(statusLine.split(" ")).get(0);
    }

    private Optional<Integer> parseHeadersForContentLength(Reader reader) {
        Optional<Integer> contentLength = Optional.empty();
        Optional<String> header;
        while((header = header(reader)).isPresent()) {
            if (isContentLength(header)) {
                contentLength = extractContentLength(header);
            }
        }
        return contentLength;
    }

    private Optional<String> header(Reader reader) {
        return readLine(reader)
          .filter(this::isNotEmpty);
    }

    private boolean isNotEmpty(String header) {
        return !header.isEmpty();
    }

    private boolean isContentLength(Optional<String> header) {
        return header
          .filter(s -> s.contains(Headers.CONTENT_LENGTH))
          .isPresent();
    }

    private Optional<Integer> extractContentLength(Optional<String> contentLength) {
        return contentLength
          .map(s -> s.substring(Headers.CONTENT_LENGTH.length()))
          .map(Integer::parseInt);
    }

    private Optional<String> getBody(Reader reader, Optional<Integer> contentLength) {
        return contentLength
          .filter(length -> length > 0)
          .flatMap(reader::read);
    }

    private Optional<HTTPRequest> request(Optional<String> method, Optional<String> body) {
        return method
          .map(HTTPRequest::new)
          .map(request -> request.withBody(body));
    }
}
