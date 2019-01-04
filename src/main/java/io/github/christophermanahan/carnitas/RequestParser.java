package io.github.christophermanahan.carnitas;

public class RequestParser implements Parser {

    private int bodyIndex;

    public String parse(String request) {
        System.out.println(request);
        bodyIndex = request.indexOf(Constants.BLANK_LINE);
        return noBody(request) ? "" : body(request);
    }

    private boolean noBody(String request) {
        return request.length() == bodyIndex;
    }

    private String body(String request) {
        return request.substring(bodyIndex + Constants.BLANK_LINE.length());
    }
}
