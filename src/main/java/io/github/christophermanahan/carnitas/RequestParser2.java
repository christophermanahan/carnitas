package io.github.christophermanahan.carnitas;

import java.util.List;
import java.util.Optional;

public class RequestParser2 implements Parser2 {
    public Optional<HTTPRequest2> parse(Reader reader) {
        Optional<String> method = getMethod(reader);
        Optional<Integer> contentLength = parseHeadersForContentLength(reader);
        Optional<String> body = getBody(reader, contentLength);
        return request(method, body);
    }

    private Optional<String> readLine(Reader reader) {
        return reader.readUntil(Constants.CRLF);
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
            contentLength = ifContentLengthGet(header);
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

    private Optional<Integer> ifContentLengthGet(Optional<String> header) {
        return header
          .filter(this::isContentLength)
          .map(this::extractContentLength);
    }

    private boolean isContentLength(String header) {
        return header.contains(Headers.CONTENT_LENGTH);
    }

    private Integer extractContentLength(String contentLength) {
        return Integer.parseInt(contentLength.substring(Headers.CONTENT_LENGTH.length()));
    }

    private Optional<String> getBody(Reader reader, Optional<Integer> contentLength) {
        return contentLength
          .filter(length -> length > 0)
          .flatMap(reader::read);
    }

    private Optional<HTTPRequest2> request(Optional<String> method, Optional<String> body) {
        return method
          .map(HTTPRequest2::new)
          .map(request -> request.withBody(body));
    }
}
