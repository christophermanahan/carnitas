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
        while (receiving) {
            receive();
            getContentLength();
            checkForContent();
            getBody();
        }
        return Optional.of(body);
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
        if (received.isEmpty()) {
            body = bufferedReceiver.receiveCharacters(contentLength);
            stopReceiving();
        }
    }

    private void stopReceiving() {
        receiving = false;
    }
}
