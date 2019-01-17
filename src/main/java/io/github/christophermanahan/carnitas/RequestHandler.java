package io.github.christophermanahan.carnitas;

public class RequestHandler implements Handler {
    static final String OK = "200 OK";
    static final String CREATED = "201 Created";

    public HTTPResponse handle(HTTPRequest request) {
        switch (request.method()) {
            case "GET":
                return new HTTPResponse(OK);
            case "HEAD":
                return new HTTPResponse(OK);
            case "POST":
                return new HTTPResponse(CREATED)
                  .withBody(request.body());
            default:
                return null;
        }
    }
}
