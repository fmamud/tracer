package com.simscale.tracer.model;

import java.util.logging.Logger;

import static java.lang.String.format;

public enum Engine {
    INMEMORY, FILE;

    private static final Logger LOGGER = Logger.getLogger(Engine.class.getName());

    public static Engine of(String engine) {
        try {
            return valueOf(engine.toUpperCase());
        } catch (IllegalArgumentException ex) {
            LOGGER.warning(format("Engine '%s' does not exists", engine));
            return INMEMORY;
        }
    }
}
