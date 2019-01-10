package io.github.christophermanahan.carnitas;

import java.util.Optional;

public interface Parser {
    public Optional<Request> parse(Receiver receiver);
}
