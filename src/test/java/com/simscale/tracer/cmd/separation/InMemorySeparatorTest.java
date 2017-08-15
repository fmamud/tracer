package com.simscale.tracer.cmd.separation;

import com.simscale.tracer.model.Database;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static com.simscale.tracer.TestData.*;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class InMemorySeparatorTest {
    @Before
    public void cleanup() {
        Database.clear();
    }

    private Separable memorySeparator = new InMemorySeparator(TRACES_INPUT);

    @Test
    public void shouldBeSeparate() throws IOException {
        String line = "2013-10-23T10:12:35.298Z 2013-10-23T10:12:35.300Z eckakaau service3 d6m3shqy->62d45qeh";
        memorySeparator.separate(line);
        assertThat(Database.get(TRACE_ID), hasItem(line));
    }

    @Test
    public void shouldBeExecute() {
        Database.put(TRACE_ID, TRACES);

        memorySeparator.execute();
        assertEquals(TRACES, Database.get(TRACE_ID));
    }
}
