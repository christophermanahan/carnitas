package io.github.christophermanahan.carnitas;

public class HTTPResponse implements Response {

    public byte[] serialize() {
        return (
          statusLine()
            + headers()
            + noBody()
        ).getBytes();
    }

    private String statusLine() {
        return Constants.VERSION + " " + StatusCodes.GET + Constants.CRLF;
    }

    private String headers() {
        return Headers.contentLength(0) + Constants.CRLF;
    }

    private String noBody() {
        return Constants.CRLF;
    }
}
