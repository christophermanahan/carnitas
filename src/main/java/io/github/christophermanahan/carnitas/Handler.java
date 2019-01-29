package io.github.christophermanahan.carnitas;

public interface Handler {
    HTTPResponse handle(HTTPRequest request);

    HTTPResponse2 handle2(HTTPRequest httpRequest);
}
