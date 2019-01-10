package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RequestHandlerTest {
    @Test
    void creates200ResponsesFromGETRequests() {
        Handler handler = new RequestHandler(new TestResponseBuilder());

        Response response = handler.handle(new GETRequest());

        String expectedResponse = StatusCodes.GET + Constants.VERSION;
        Assertions.assertEquals(expectedResponse, new String(response.serialize()));
    }

    @Test
    void creates201ResponsesFromPOSTRequests() {
        String body = "name=<something>";
        Handler handler = new RequestHandler(new TestResponseBuilder());

        Response response = handler.handle(new POSTRequest(body));

        String expectedResponse = StatusCodes.POST + Constants.VERSION + body;
        Assertions.assertEquals(expectedResponse, new String(response.serialize()));
    }

    private class TestResponseBuilder implements ResponseBuilder {
        private String statusCode;
        private String version;
        private String body;

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
            return this;
        }

        public Response build() {
            return body == null ? new TestResponse(statusCode + version) : new TestResponse(statusCode + version + body);
        }
    }

    private class TestResponse implements Response {
        private final String response;

        TestResponse(String response) {
            this.response = response;
        }

        public byte[] serialize() {
            return response.getBytes();
        }
    }

    private class GETRequest implements Request {
        public RequestMethod requestMethod() {
            return RequestMethod.GET;
        }

        public String body() {
            return null;
        }
    }

    private class POSTRequest implements Request {
        private final String body;

        public POSTRequest(String body) {
            this.body = body;
        }

        public RequestMethod requestMethod() {
            return RequestMethod.POST;
        }

        public String body() {
            return body;
        }
    }
}
