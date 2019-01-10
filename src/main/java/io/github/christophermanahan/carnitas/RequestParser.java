package io.github.christophermanahan.carnitas;

import java.util.List;
import java.util.Optional;

public class RequestParser implements Parser {
    private final RequestBuilder requestBuilder;
    private Receiver receiver;
    private boolean receiving = true;
    private String requestMethod;
    private int contentLength = -1;
    private String body;

    RequestParser(RequestBuilder requestBuilder) {
       this.requestBuilder = requestBuilder;
    }

    public Optional<Request> parse(Receiver receiver) {
        this.receiver = receiver;
        try {
            parseStatusLine(receiver.receiveLine());
            parseRequest();
            return Optional.of(buildRequest());
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    private void parseStatusLine(String statusLine) {
        requestMethod = List.of(statusLine.split(" ")).get(0);
    }

    private void parseRequest() {
        while (receiving) {
            parseLine(receiver.receiveLine());
        }
    }

    private void parseLine(String received) {
        getContentLength(received);
        checkForContent();
        getBody(received);
        endRequest(received);
    }

    private void getContentLength(String received) {
        if (received.contains(Headers.CONTENT_LENGTH)) {
            this.contentLength = Integer.parseInt(received.substring(Headers.CONTENT_LENGTH.length()));
        }
    }

    private void checkForContent() {
        if (contentLength == 0) {
            stopReceiving();
        }
    }

    private void getBody(String received) {
        if (contentLength > 0 && received.isEmpty()) {
            this.body = receiver.receiveCharacters(contentLength);
            stopReceiving();
        }
    }

    private void endRequest(String received) {
        if (received.isEmpty()) {
            stopReceiving();
        }
    }

    private void stopReceiving() {
        receiving = false;
    }

    private Request buildRequest() {
        return requestBuilder
          .requestMethod(requestMethod)
          .body(body)
          .build();
    }
}
