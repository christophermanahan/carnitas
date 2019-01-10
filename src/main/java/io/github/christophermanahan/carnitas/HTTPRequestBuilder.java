package io.github.christophermanahan.carnitas;

public class HTTPRequestBuilder implements RequestBuilder {
    private RequestMethod requestMethod;
    private String body;

    public RequestBuilder requestMethod(String requestMethod) {
        this.requestMethod = RequestMethod.valueOf(requestMethod);
        return this;
    }

    public RequestBuilder body(String body) {
        this.body = body;
        return this;
    }

    public Request build() {
        return new HTTPRequest(requestMethod, body);
    }
}
