package io.github.christophermanahan.carnitas;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(port(args))) {
            new HTTPServer(
              new RequestParser(),
              router(),
              new ErrorLogger()
            ).start(
              new ConnectionListener(serverSocket),
              new WhileOpen(serverSocket)
            );
        } catch (IOException e) {
            new ErrorLogger().log(e.getMessage());
        }
    }

    private static int port(String[] args) {
        return Optional.of(args)
          .filter(a -> a.length > 0)
          .map(b -> b[0])
          .map(Integer::parseInt)
          .orElse(33333);
    }

    private static Handler router() {
        return new Router()
          .get("/simple_get", (HTTPRequest request) -> new HTTPResponse(HTTPResponse.StatusCode.OK))
          .head("/simple_get", (HTTPRequest request) -> new HTTPResponse(HTTPResponse.StatusCode.OK))
          .post("/simple_post", (HTTPRequest request) -> new HTTPResponse(HTTPResponse.StatusCode.CREATED)
            .withBody(request.body())
          );
    }
}
