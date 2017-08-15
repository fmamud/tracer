package com.simscale.tracer.cmd.processing;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.simscale.tracer.TestData.*;
import static com.simscale.tracer.model.Directory.TRACE_DIR;
import static org.junit.Assert.assertEquals;

public class FileProcessorTest {

    @BeforeClass
    public static void setup() throws IOException {
        File outputFile = new File(TRACE_DIR, TRACE_ID);
        outputFile.deleteOnExit();

        Files.write(outputFile.toPath(), TRACES);
    }

    private ByteArrayOutputStream TRACES_OUTPUT = new ByteArrayOutputStream();
    private Processable memoryProcessor = new FileProcessor(TRACES_OUTPUT);

    @Test
    public void shouldBeProcess() {
        memoryProcessor.execute();
        assertEquals(JSON_TREE.concat("\n"), TRACES_OUTPUT.toString());
    }
}
