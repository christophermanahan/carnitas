package io.github.christophermanahan.carnitas;

import java.util.Optional;

public interface Parser2 {
    Optional<HTTPRequest2> parse(ConnectionReader reader);
}
