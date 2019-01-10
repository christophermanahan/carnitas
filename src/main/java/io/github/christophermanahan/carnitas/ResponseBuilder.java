package io.github.christophermanahan.carnitas;

public interface ResponseBuilder {
    public ResponseBuilder statusCode(String statusCode);
    public ResponseBuilder version(String version);
    public ResponseBuilder body(String body);
    public Response build();
}
