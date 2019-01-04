package io.github.christophermanahan.carnitas;

public class HTTPResponse implements Response {

    private String statusCode;
    private String body;

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
        return Headers.contentLength(body.length()) + Constants.CRLF;
    }

    private String body() {
        return Constants.CRLF + body;
    }

    private String statusCode() {
        return body.isEmpty() ? StatusCodes.GET : StatusCodes.POST;
    }
}
