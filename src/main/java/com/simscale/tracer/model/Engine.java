package com.simscale.tracer.model;

import static java.lang.String.format;

public enum Engine {
    INMEMORY, FILE;

    public static Engine of(String engine) {
        try {
            return valueOf(engine.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(format("Engine '%s' does not exists", engine), ex);
        }
    }
}
