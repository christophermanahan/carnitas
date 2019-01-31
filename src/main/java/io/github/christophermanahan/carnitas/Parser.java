package io.github.christophermanahan.carnitas;

import java.util.Optional;

public interface Parser<T, R> {
    Optional<HTTPRequest> parse(Reader reader);

    R parse(T request);
}
