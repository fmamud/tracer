package com.simscale.tracer.model;

import org.junit.Before;
import org.junit.Test;

import static com.simscale.tracer.TestData.TRACES;
import static com.simscale.tracer.TestData.TRACE_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseTest {
    @Before
    public void cleanup() {
        Database.clear();
    }

    @Test
    public void testPutOneLine() {
        Database.put(TRACE_ID, TRACES.get(0));
        assertEquals(TRACES.get(0), Database.get(TRACE_ID).get(0));
    }

    @Test
    public void testPutLines() {
        Database.put(TRACE_ID, TRACES);
        assertEquals(TRACES, Database.get(TRACE_ID));
    }

    @Test
    public void testStreamLines() {
        Database.put(TRACE_ID, TRACES);
        assertTrue(Database.stream().anyMatch(e -> TRACE_ID.equals(e.getKey())));
    }

    @Test
    public void testClear() {
        Database.put(TRACE_ID, TRACES);
        Database.clear();
        assertTrue(Database.isEmpty());
    }
}
