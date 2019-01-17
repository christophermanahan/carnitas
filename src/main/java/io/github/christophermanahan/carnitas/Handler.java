package io.github.christophermanahan.carnitas;

public interface Handler {
    HTTPResponse handle(HTTPRequest request);
}
