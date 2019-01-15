package io.github.christophermanahan.carnitas;

import java.util.List;
import java.util.Optional;

public class RequestParser implements Parser {
    private final RequestBuilder requestBuilder;
    private Receiver receiver;
    private boolean receiving = true;
    private String requestMethod;
    private int contentLength = -1;
    private String body = "";

    RequestParser(RequestBuilder requestBuilder) {
       this.requestBuilder = requestBuilder;
    }

    public Optional<Request> parse(Receiver receiver) {
        this.receiver = receiver;
        resetState();
        try {
            parseStatusLine(receiver.receiveLine());
            parseRequest();
            return Optional.of(buildRequest());
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    private void resetState() {
        this.receiving = true;
        this.contentLength = -1;
        this.body = "";
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
        getBody(received);
        endRequest(received);
    }

    private void getContentLength(String received) {
        if (isPostWithBody(received)) {
            contentLength = Integer.parseInt(received.substring(Headers.CONTENT_LENGTH.length()));
        }
    }

    private boolean isPostWithBody(String received) {
        return RequestMethod.valueOf(requestMethod) == RequestMethod.POST && received.contains(Headers.CONTENT_LENGTH);
    }

    private void getBody(String received) {
        if (contentLength > 0 && received.isEmpty()) {
            body = receiver.receiveCharacters(contentLength);
//            body = receiver.receiveLine();
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
