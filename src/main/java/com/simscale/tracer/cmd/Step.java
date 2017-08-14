package com.simscale.tracer.cmd;

import java.util.logging.Logger;
import java.util.stream.Stream;

public interface Step {
    void execute();

    default Logger log() {
        return Logger.getLogger(Step.class.getName());
    }

    static void sequence(Step... steps) {
        Stream.of(steps).forEach(Step::execute);
    }
}
