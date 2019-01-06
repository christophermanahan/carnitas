package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConnectionReceiverTest {

    @Test
    void receivesDataFromStream() {
        String request = "GET /simple_get HTTP1.1";

        String received = new ConnectionReceiver(new ByteArrayInputStream(request.getBytes())).receiveLine();

        assertEquals(request, received);
    }

    @Test
    void receivesCharacterAmountFromStream() {
        String request = "GET /simple_get HTTP1.1";
        int amount = 3;

        String received = new ConnectionReceiver(new ByteArrayInputStream(request.getBytes())).receiveCharacters(amount);

        assertEquals(request.substring(0, amount), received);
    }
}
