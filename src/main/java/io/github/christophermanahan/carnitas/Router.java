package io.github.christophermanahan.carnitas;

import java.util.function.Function;

interface Router {
    RequestRouter get(String uri, Function<HTTPRequest2, HTTPResponse> handler);

    RequestRouter head(String uri, Function<HTTPRequest2, HTTPResponse> handler);

    RequestRouter post(String uri, Function<HTTPRequest2, HTTPResponse> handler);

    HTTPResponse process(HTTPRequest2 request);
}
