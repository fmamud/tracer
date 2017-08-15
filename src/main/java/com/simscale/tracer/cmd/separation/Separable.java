package com.simscale.tracer.cmd.separation;

import com.simscale.tracer.cmd.Step;
import com.simscale.tracer.model.Engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.lang.String.format;

public interface Separable extends Step {
    void separate(String line) throws IOException;

    InputStream stream();

    @Override
    default void execute() {
        String line = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream()))) {
            while ((line = br.readLine()) != null) {
                separate(line);
            }
        } catch (IOException e) {
            log().severe(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException ex) {
            log().warning(format("trace parser error -> %s", line));
        }
    }

    static Separable create(Engine engine, InputStream stream) {
        switch (engine) {
            case INMEMORY:
                return new InMemorySeparator(stream);
            case FILE:
                return new FileSeparator(stream);
            default:
                throw new IllegalArgumentException(format("Engine '%s' does not Separable implementation", engine.name()));
        }
    }
}
