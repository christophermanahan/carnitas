package io.github.christophermanahan.carnitas;

public class HTTPResponse implements Response {

    static final String VERSION = "HTTP/1.1";
    static final String GET = "200 OK";
    static final String CL = "Content-Length: 0";
    static final String CRLF = "\r\n";

    public byte[] serialize() {
        return (
          statusLine()
            + headers()
            + noBody()
        ).getBytes();
    }

    private String statusLine() {
        return VERSION + " " + GET + CRLF;
    }

    private String headers() {
        return CL + CRLF;
    }

    private String noBody() {
        return CRLF;
    }
}
