package com.simscale.tracer.cmd;

import java.util.stream.Stream;

public interface Step {
    void execute();

    static void sequence(Step... steps) {
        Stream.of(steps).forEach(Step::execute);
    }
}
