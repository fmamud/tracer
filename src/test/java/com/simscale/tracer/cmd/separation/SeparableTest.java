package com.simscale.tracer.cmd.separation;

import com.simscale.tracer.TestData;
import com.simscale.tracer.model.Engine;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class SeparableTest {
    @Test
    public void testCreate() {
        Separable separable = Separable.create(TestData.TRACES_INPUT);
        assertThat(separable, instanceOf(InMemorySeparator.class));
    }

    @Test
    public void testCreateWithEngine() {
        Separable separable = Separable.create(Engine.FILE, TestData.TRACES_INPUT);
        assertThat(separable, instanceOf(FileSeparator.class));
    }
}
