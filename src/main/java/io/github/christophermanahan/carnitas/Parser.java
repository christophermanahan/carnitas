package io.github.christophermanahan.carnitas;

import java.util.Optional;

public interface Parser {
    Optional<HTTPRequest> parse(Reader reader);
}
