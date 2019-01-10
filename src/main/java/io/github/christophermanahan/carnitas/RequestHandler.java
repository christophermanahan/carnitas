package io.github.christophermanahan.carnitas;

public class RequestHandler implements Handler {
    private final ResponseBuilder responseBuilder;
    private String statusCode;
    private String body;

    RequestHandler(ResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    public Response handle(Request request) {
        statusCode(request);
        body(request);
        return buildResponse();
    }

    private void statusCode(Request request) {
        switch (request.requestMethod()) {
            case GET:
                this.statusCode = StatusCodes.GET;
                break;

            case POST:
                this.statusCode = StatusCodes.POST;
                break;

            default:
                this.statusCode = StatusCodes.GET;
                break;
        }
    }

    private void body(Request request) {
        body = request.body();
    }

    private Response buildResponse() {
        return responseBuilder
          .statusCode(statusCode)
          .version(Constants.VERSION)
          .body(body)
          .build();
    }
}
