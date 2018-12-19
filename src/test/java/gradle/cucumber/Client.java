package gradle.cucumber;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client {

  private final HttpClient client;

  public Client() {
    client = HttpClient.newHttpClient();
  }

  public HttpResponse<String> request(String port, String method, String location) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("http://localhost:" + port + location))
      .method(method, HttpRequest.BodyPublishers.noBody())
      .build();
    return client.send(request, HttpResponse.BodyHandlers.ofString());
  }
}
