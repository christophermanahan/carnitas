package io.github.christophermanahan.carnitas;

public class HTTPResponse implements Response {

    private String body;
    private String statusCode;

    public HTTPResponse(String body) {
       this.body = body;
       this.statusCode = statusCode();
    }

    public byte[] serialize() {
        return (
          statusLine()
            + headers()
            + body()
        ).getBytes();
    }

    private String statusLine() {
        return Constants.VERSION + " " + statusCode + Constants.CRLF;
    }

    private String headers() {
        return Headers.CONTENT_LENGTH + body.length();
    }

    private String body() {
        return Constants.BLANK_LINE + body;
    }

    private String statusCode() {
        return body.isEmpty() ? StatusCodes.GET : StatusCodes.POST;
    }
}
