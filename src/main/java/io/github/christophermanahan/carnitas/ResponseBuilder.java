package io.github.christophermanahan.carnitas;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ResponseBuilder implements Supplier<HTTPResponse2> {
    private HTTPResponse2.Status status;
    private List<String> headers = new ArrayList<>();
    private Optional<String> body = Optional.empty();

    public ResponseBuilder setStatus(HTTPResponse2.Status status) {
        this.status = status;
        return this;
    }

    public ResponseBuilder addHeader(String header) {
        this.headers.add(header);
        return this;
    }

    public ResponseBuilder setBody(Optional<String> body) {
        this.body = body;
        return this;
    }

    public HTTPResponse2 get() {
        return new HTTPResponse2(status, headers, body);
    }

}
