package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

class ErrorLoggerTest {
    @Test
    void logsErrorMessagesAppendedWithNewLine() {
        String error = "Failed due to <error>";
        OutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        Logger logger = new ErrorLogger();

        logger.log(error);

        Assertions.assertEquals(error.concat("\r\n"), output.toString());
    }
}
