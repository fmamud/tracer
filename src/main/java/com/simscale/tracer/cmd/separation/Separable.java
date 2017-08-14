package com.simscale.tracer.cmd.separation;

import com.simscale.tracer.cmd.Step;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public interface Separable extends Step {

    void separate(String line);

    InputStream stream();

    @Override
    default void execute() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                separate(line);
            }
        } catch (IOException e) {
            log().severe(e.getMessage());
        }
    }
}
