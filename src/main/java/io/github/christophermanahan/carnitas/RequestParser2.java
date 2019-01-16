package io.github.christophermanahan.carnitas;

import java.util.List;
import java.util.Optional;

public class RequestParser2 implements Parser2 {
    public Optional<HTTPRequest2> parse(Reader reader) {
        String method = method(reader);
        int contentLength = contentLength(reader);
        String body = body(reader, contentLength);
        return request(method, body);
    }

    private String readLine(Reader reader) {
        return reader.readUntil(Constants.CRLF);
    }

    private String method(Reader reader) {
        return List.of(readLine(reader).split(" ")).get(0);
    }

    private int contentLength(Reader reader) {
        int contentLength = 0;
        String read;
        while (!(read = readLine(reader)).isEmpty()) {
            contentLength = checkForContentLength(read);
        }
        return contentLength;
    }

    private int checkForContentLength(String read) {
        if (read.contains(Headers.CONTENT_LENGTH)) {
            return Integer.parseInt(read.substring(Headers.CONTENT_LENGTH.length()));
        }
        return 0;
    }

    private String body(Reader reader, int contentLength) {
        return contentLength > 0 ? reader.read(contentLength) : null;
    }

    private Optional<HTTPRequest2> request(String method, String body) {
        return Optional.of(new HTTPRequest2(method)
          .withBody(body)
        );
    }
}
