package io.github.christophermanahan.carnitas;

import java.util.HashSet;
import java.util.Optional;
import java.util.function.Supplier;

public class ResponseBuilder implements Supplier<HTTPResponse> {
    private HTTPResponse.Status status = HTTPResponse.Status.OK;
    private HashSet<String> headers = new HashSet<>();
    private Optional<String> body = Optional.empty();

    public ResponseBuilder set(HTTPResponse.Status status) {
        this.status = status;
        return this;
    }

    public ResponseBuilder add(String header) {
        headers.add(header);
        return this;
    }

    public ResponseBuilder set(Optional<String> body) {
        this.body = body;
        return this;
    }

    public HTTPResponse get() {
        return new HTTPResponse(status, headers, body);
    }

}
