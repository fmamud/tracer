package com.simscale.tracer.cmd.processing;

import com.simscale.tracer.model.Database;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.stream.Stream.empty;

public class InMemoryProcessor implements Processable<Map.Entry<String, List<String>>> {
    private OutputStream output;

    private static final Logger LOGGER = Logger.getLogger(InMemoryProcessor.class.getName());

    public InMemoryProcessor(OutputStream output) {
        this.output = output;
    }

    @Override
    public OutputStream stream() {
        return output;
    }

    @Override
    public Stream<Map.Entry<String, List<String>>> data() {
        return Database.stream();
    }

    @Override
    public Function<Map.Entry<String, List<String>>, String> id() {
        return Map.Entry::getKey;
    }

    @Override
    public Function<Map.Entry<String, List<String>>, Stream<String>> lines() {
        return entry -> Optional.ofNullable(Database.get(entry.getKey())).map(List::stream).orElse(empty());
    }

    @Override
    public Logger log() {
        return LOGGER;
    }
}
