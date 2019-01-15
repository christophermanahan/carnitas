package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPResponse2Test {
    @Test
    void itSerializesToAFormattedHTTPResponse() {
        String statusCode = "200 OK";
        HTTPResponse2 httpResponse = new HTTPResponse2(statusCode);

        String response = new String(httpResponse.serialize());

        String expectedResponse = Constants.VERSION +  " " + statusCode + Constants.BLANK_LINE;
        assertEquals(expectedResponse, response);
    }
}
