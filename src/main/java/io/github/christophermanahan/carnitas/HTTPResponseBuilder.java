package io.github.christophermanahan.carnitas;

public class HTTPResponseBuilder implements ResponseBuilder {
    private String statusCode = "";
    private String version = "";
    private String body = "";
    private String headers = "";

    public ResponseBuilder statusCode(String statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public ResponseBuilder version(String version) {
        this.version = version;
        return this;
    }

    public ResponseBuilder body(String body) {
        this.body = body;
        this.headers = Headers.CONTENT_LENGTH + body.length();
        return this;
    }

    public Response build() {
        return new HTTPResponse(statusCode, version, body, headers);
    }
}
