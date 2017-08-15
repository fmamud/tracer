package com.simscale.tracer.cmd.separation;

import com.simscale.tracer.model.Database;

import java.io.InputStream;

public class InMemorySeparator implements Separable {

    private InputStream input;

    public InMemorySeparator(InputStream input) {
        this.input = input;
    }

    @Override
    public void separate(String line) {
        Database.put(line.split("\\s+")[2], line);
    }

    @Override
    public InputStream stream() {
        return input;
    }
}
