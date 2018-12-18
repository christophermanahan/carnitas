package io.github.christophermanahan.carnitas;

public class HTTPServer {

  private final Listener listener;
  private final Logger errorLogger;
  private Connection connection;

  public HTTPServer(Listener listener, Logger errorLogger) {
    this.listener = listener;
    this.errorLogger = errorLogger;
  }

  public void run() {
    try {
      connect();
      serve();
      close();
    } catch (RuntimeException e) {
      errorLogger.log(e.getMessage());
    }
  }

  private void connect() {
    connection = listener.listen();
  }

  private void serve() {
    connection.receive()
      .map(HTTPResponse::new)
      .ifPresent(connection::send);
  }

  private void close() {
    connection.close();
  }
}
