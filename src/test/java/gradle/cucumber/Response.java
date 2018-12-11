package gradle.cucumber;

public class Response {
  private final String data;

  public Response(String data) {
    this.data = data;
  }

  public String contains() {
    return data;
  }
}
