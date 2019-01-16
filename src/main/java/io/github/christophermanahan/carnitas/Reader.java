package io.github.christophermanahan.carnitas;

import java.util.Optional;

public interface Reader {
    Optional<String> readUntil(String delimiter);
    Optional<String> read(int numberOfCharacters);
}
