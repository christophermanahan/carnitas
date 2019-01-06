package gradle.cucumber;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client {

    private final HttpClient client;
    private HttpRequest.BodyPublisher bodyPublisher;

    public Client() {
        client = HttpClient.newHttpClient();
    }

    public HttpResponse<String> request(String port, String method, String location, String body) throws IOException, InterruptedException {
        bodyPublisher = body.isEmpty() ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + port + location))
            .version(HttpClient.Version.valueOf("HTTP_1_1"))
            .method(method, bodyPublisher)
            .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
