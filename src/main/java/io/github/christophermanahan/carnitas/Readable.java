package io.github.christophermanahan.carnitas;

import java.util.Optional;

interface Readable {
    Optional<Character> read();
    Optional<byte[]> readAll();
}
