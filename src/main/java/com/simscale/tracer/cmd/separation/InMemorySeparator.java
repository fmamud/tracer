package com.simscale.tracer.cmd.separation;

import com.simscale.tracer.Main;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

public class InMemorySeparator implements Separable {

    private InputStream input;

    public InMemorySeparator(InputStream input) {
        this.input = input;
    }

    @Override
    public void separate(String line) {
        String key = line.split("\\s+")[2];
        List<String> lines = ofNullable(Main.data.get(key)).orElse(new ArrayList<>());
        lines.add(line);
        Main.data.put(key, lines);
    }

    @Override
    public InputStream stream() {
        return input;
    }
}
