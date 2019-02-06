package io.github.christophermanahan.carnitas;

public class ServerLogger implements Logger {
    public void log(String message) {
        System.out.println(message);
    }

    public Request log(Request request) {
        System.out.println(request.toString());
        return request;
    }

    public Response log(Response response) {
        System.out.println(response.toString());
        return response;
    }
}
