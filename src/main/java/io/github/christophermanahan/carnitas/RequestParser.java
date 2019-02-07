package io.github.christophermanahan.carnitas;

import java.util.List;
import java.util.Optional;

public class RequestParser implements Parser {
    public Optional<Request> parse(Reader reader) {
        Optional<String> statusLine = readLine(reader);
        Optional<String> method = getMethod(statusLine);
        Optional<String> uri = getUri(statusLine);
        Optional<Integer> contentLength = parseHeadersForContentLength(reader);
        Optional<String> body = getBody(reader, contentLength);
        return request(method, uri, body);
    }

    private Optional<String> getMethod(Optional<String> statusLine) {
        return statusLine
          .map(this::extractMethod);
    }

    private Optional<String> getUri(Optional<String> statusLine) {
        return statusLine
          .map(this::extractUri);
    }

    private Optional<String> readLine(Reader reader) {
        return reader.readUntil(Response.CRLF);
    }

    private String extractMethod(String statusLine) {
        return List.of(statusLine.split(" ")).get(0);
    }

    private String extractUri(String statusLine) {
        return List.of(statusLine.split(" ")).get(1);
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

    private Optional<Request> request(Optional<String> method, Optional<String> uri, Optional<String> body) {
        if (method.isPresent() && uri.isPresent()) {
            return Optional.of(
              new Request(
                Request.Method.valueOf(method.get()),
                uri.get()
              ).withBody(body)
            );
        } else {
            return Optional.empty();
        }
    }
}
