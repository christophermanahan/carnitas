package io.github.christophermanahan.carnitas;

public class RequestHandler2 implements Handler2 {
    public HTTPResponse2 handle(HTTPRequest2 request) {
        switch (request.method()) {
            case "GET":
                return new HTTPResponse2("200 OK");
            case "HEAD":
                return new HTTPResponse2("200 OK");
            case "POST":
                return new HTTPResponse2("201 Created")
                  .withBody(request.body());
            default:
                return null;
        }
    }
}
