package io.github.christophermanahan.carnitas;

import java.util.Optional;

public class RequestParser implements Parser {

    private boolean receiving = true;
    private String body = "";
    private int contentLength = -1;
    private String received;
    private Receiver bufferedReceiver;

    public Optional<String> parse(Receiver receiver) {
        bufferedReceiver = receiver;
        try {
            while (receiving) {
                parseRequest();
            }
            return Optional.of(body);
        } catch(RuntimeException e) {
            return Optional.empty();
        }
    }

    private void parseRequest() {
        receive();
        getContentLength();
        checkForContent();
        getBody();
    }

    private void receive() {
        received = bufferedReceiver.receiveLine();
    }

    private void getContentLength() {
        if (received.contains(Headers.CONTENT_LENGTH)) {
            contentLength = Integer.parseInt(received.substring(Headers.CONTENT_LENGTH.length()));
        }
    }

    private void checkForContent() {
        if (contentLength == 0) {
            stopReceiving();
        }
    }

    private void getBody() {
        if (contentLength > 0 && received.isEmpty()) {
            body = bufferedReceiver.receiveCharacters(contentLength);
            stopReceiving();
        }
    }

    private void stopReceiving() {
        receiving = false;
    }
}
