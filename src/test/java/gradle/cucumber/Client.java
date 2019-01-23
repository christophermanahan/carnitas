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
        this.client = HttpClient.newHttpClient();
        this.httpBody = HttpRequest.BodyPublishers.noBody();
    }

    HttpResponse<String> request(String port, String method, String uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create("http://localhost:" + port + uri))
          .version(HttpClient.Version.HTTP_1_1)
          .method(method, httpBody)
          .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    Client withBody(String body) {
        this.httpBody = HttpRequest.BodyPublishers.ofString(body);
        return this;
    }
}
