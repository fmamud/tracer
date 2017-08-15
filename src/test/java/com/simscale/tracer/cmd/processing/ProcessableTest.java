package com.simscale.tracer.cmd.processing;

import com.simscale.tracer.model.Database;
import com.simscale.tracer.model.Engine;
import com.simscale.tracer.model.ast.NodeTree;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.AbstractMap;

import static com.simscale.tracer.TestData.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ProcessableTest {

    @BeforeClass
    public static void setup() {
        Database.clear();
    }

    @Before
    public void populating() {
        Database.put(TRACE_ID, TRACES);
    }

    @Test
    public void testCreate() {
        Processable processable = Processable.create(new ByteArrayOutputStream());
        assertThat(processable, instanceOf(InMemoryProcessor.class));
    }

    @Test
    public void testCreateWithEngine() {
        Processable processable = Processable.create(Engine.FILE, new ByteArrayOutputStream());
        assertThat(processable, instanceOf(FileProcessor.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testNodeTree() {
        Processable processable = Processable.create(new ByteArrayOutputStream());

        NodeTree nodeTree = processable.nodeTree(new AbstractMap.SimpleEntry<>(TRACE_ID, TRACES));
        assertEquals(JSON_TREE, nodeTree.toString());
    }
}
