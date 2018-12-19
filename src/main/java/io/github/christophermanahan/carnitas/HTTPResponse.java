package io.github.christophermanahan.carnitas;

public class HTTPResponse implements Response {

  static final String VERSION = "HTTP/1.1";
  static final String GET = "200 OK";
  static final String CRLF = "\r\n";

  public byte[] bytes() {
    return String.format("%s %s%s%s", VERSION, GET, CRLF, CRLF).getBytes();
  }
}
