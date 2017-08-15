package com.simscale.tracer.cmd.processing;

import com.simscale.tracer.model.Database;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static com.simscale.tracer.TestData.*;
import static org.junit.Assert.assertEquals;

public class InMemoryProcessorTest {

    @BeforeClass
    public static void setup() {
        Database.clear();
    }

    @Before
    public void populating() {
        Database.put(TRACE_ID, TRACES);
    }

    private ByteArrayOutputStream TRACES_OUTPUT = new ByteArrayOutputStream();
    private Processable memoryProcessor = new InMemoryProcessor(TRACES_OUTPUT);

    @Test
    public void shouldBeProcess() {
        memoryProcessor.execute();
        assertEquals(JSON_TREE.concat("\n"), TRACES_OUTPUT.toString());
    }
}
