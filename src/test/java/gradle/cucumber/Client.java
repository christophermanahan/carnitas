package gradle.cucumber;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class Client {
    private final HttpClient client;
    private HttpRequest.BodyPublisher httpBody;

    Client() {
        client = HttpClient.newHttpClient();
        httpBody = HttpRequest.BodyPublishers.noBody();
    }

    HttpResponse<String> request(String port, String method, String location) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + port + location))
            .method(method, httpBody)
            .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    Client setBody(String body) {
        httpBody = HttpRequest.BodyPublishers.ofString(body);
        return this;
    }
}
