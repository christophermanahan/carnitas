package io.github.christophermanahan.carnitas;

import java.util.Optional;

public interface Connection {
  Optional<String> receive();

  void send(String data);

  void close();
}
