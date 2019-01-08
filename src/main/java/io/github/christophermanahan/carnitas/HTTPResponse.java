package io.github.christophermanahan.carnitas;

public class HTTPResponse implements Response {
    static final String VERSION = "HTTP/1.1";
    static final String GET = "200 OK";
    static final String CL = "Content-Length: 0";
    static final String CRLF = "\r\n";

    public byte[] serialize() {
        return String.format("%s %s%s%s%s%s", VERSION, GET, CRLF, CL, CRLF, CRLF).getBytes();
    }
}
