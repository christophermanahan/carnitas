package io.github.christophermanahan.carnitas;

import java.util.Optional;

public interface Parser {
    Optional<Request> parse(Reader reader);
}
