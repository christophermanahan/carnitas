package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RequestParserTest {

    @Test
    void parsesRequestWithoutBody() {
        String body = "";
        String request = "GET HTTP/1.1 http://localhost:80/simple_post" + Constants.BLANK_LINE + body;
        Parser parser = new RequestParser();

        String parsed = parser.parse(request);

        Assertions.assertEquals(body, parsed);
    }

    @Test
    void parsesRequestWithBody() {
        String body = "hello world";
        String request = "POST HTTP/1.1 http://localhost:80/simple_post" + Constants.BLANK_LINE + body;
        Parser parser = new RequestParser();

        String parsed = parser.parse(request);

        Assertions.assertEquals(body, parsed);
    }
}
