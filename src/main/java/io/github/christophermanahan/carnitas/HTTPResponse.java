package io.github.christophermanahan.carnitas;

public class HTTPResponse implements Response {
    private final String statusCode;
    private final String version;
    private final String body;
    private final String headers;

    HTTPResponse(String statusCode, String version, String body, String header) {
        this.statusCode = statusCode;
        this.version = version;
        this.body = body;
        this.headers = header;
    }

    public byte[] serialize() {
        return (
          version + " " + statusCode + Constants.CRLF
            + headers
            + Constants.BLANK_LINE
            + body
        ).getBytes();
    }
}
