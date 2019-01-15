package io.github.christophermanahan.carnitas;

public class HTTPResponse2 implements Response {
    private final String statusCode;

    HTTPResponse2(String statusCode) {
        this.statusCode = statusCode;
    }

    public byte[] serialize() {
        return (Constants.VERSION + " " + statusCode + Constants.BLANK_LINE).getBytes();
    }
}
