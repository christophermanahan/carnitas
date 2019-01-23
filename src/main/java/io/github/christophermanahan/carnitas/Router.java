package io.github.christophermanahan.carnitas;

import java.util.function.Function;

interface Router {
    RequestRouter get(String uri, Function<HTTPRequest, HTTPResponse> handler);

    RequestRouter head(String uri, Function<HTTPRequest, HTTPResponse> handler);

    RequestRouter post(String uri, Function<HTTPRequest, HTTPResponse> handler);

    HTTPResponse handle(HTTPRequest request);
}
