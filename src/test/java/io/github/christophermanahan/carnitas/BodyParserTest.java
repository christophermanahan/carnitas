package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

class BodyParserTest {
    @Test
    void itParsesTheRequestBody() {
        String restOfRequest = "name=<something>";
        RequestBuilder builder = new RequestBuilder();
        Parser<Iterator<String>, RequestBuilder> parser = new BodyParser(builder);

        parser.parse(Collections.singletonList(restOfRequest).iterator());

        Assertions.assertEquals(Optional.of(restOfRequest), builder.get().body());
    }

    @Test
    void itProvidesTheBuilder() {
        String restOfRequest = "name=<something>";
        Parser<Iterator<String>, RequestBuilder> parser = new BodyParser(new RequestBuilder());

        RequestBuilder builder = parser.parse(Collections.singletonList(restOfRequest).iterator());

        Assertions.assertEquals(Optional.of(restOfRequest), builder.get().body());

    }
}
