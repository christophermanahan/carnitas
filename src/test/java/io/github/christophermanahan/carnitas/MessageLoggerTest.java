package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageLoggerTest {
    @Test
    void itLogsErrorMessagesAppendedWithNewLine() {
        String error = "Failed due to <error>";
        OutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        Logger logger = new MessageLogger();

        logger.log(error);

        assertEquals(error, output.toString().strip());
    }
}
