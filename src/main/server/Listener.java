package server;

import java.io.IOException;

public interface Listener {
  Client listenForClient() throws IOException;
}
