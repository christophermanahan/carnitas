package io.github.christophermanahan.carnitas;

public class RequestHandler implements Handler {
    public HTTPResponse handle(HTTPRequest request) {
        switch (request.method()) {
            case "GET":
                return new HTTPResponse("200 OK");
            case "HEAD":
                return new HTTPResponse("200 OK");
            case "POST":
                return new HTTPResponse("201 Created")
                  .withBody(request.body());
            default:
                return null;
        }
    }
}
