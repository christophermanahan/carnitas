package io.github.christophermanahan.carnitas;

import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Supplier;

public class ResponseBuilder implements Supplier<Response> {
    private Response.Status status = Response.Status.OK;
    private TreeSet<String> headers = new TreeSet<>();
    private Optional<String> body = Optional.empty();

    public ResponseBuilder set(Response.Status status) {
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

    public Response get() {
        return new Response(status, headers, body);
    }

}
