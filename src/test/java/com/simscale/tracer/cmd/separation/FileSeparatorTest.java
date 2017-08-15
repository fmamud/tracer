package com.simscale.tracer.cmd.separation;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;

import static com.simscale.tracer.TestData.*;
import static com.simscale.tracer.model.Directory.TRACE_PATH;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class FileSeparatorTest {
    private Separable fileSeparator = new FileSeparator(TRACES_INPUT);

    @Before
    public void cleanup() throws IOException {
        Files.deleteIfExists(TRACE_PATH.resolve(TRACE_ID));
    }

    @Test
    public void shouldBeSeparate() throws IOException {
        String line = "2013-10-23T10:12:35.298Z 2013-10-23T10:12:35.300Z eckakaau service3 d6m3shqy->62d45qeh";
        fileSeparator.separate(line);
        assertThat(Files.readAllLines(TRACE_PATH.resolve(TRACE_ID)), hasItem(line));
    }

    @Test
    public void shouldBeExecute() throws IOException {
        fileSeparator.execute();
        assertEquals(TRACES, Files.readAllLines(TRACE_PATH.resolve(TRACE_ID)));
    }
}
