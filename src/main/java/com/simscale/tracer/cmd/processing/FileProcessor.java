package com.simscale.tracer.cmd.processing;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static com.simscale.tracer.model.Directory.TRACE_PATH;
import static java.util.stream.Stream.empty;

public class FileProcessor implements Processable<Path> {
    private OutputStream output;

    private static final Logger LOGGER = Logger.getLogger(FileProcessor.class.getName());

    public FileProcessor(OutputStream output) {
        this.output = output;
    }

    @Override
    public OutputStream stream() {
        return output;
    }

    @Override
    public Stream<Path> data() {
        try {
            return Files.list(TRACE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return empty();
    }

    @Override
    public Function<Path, String> id() {
        return path -> path.getFileName().toString();
    }

    @Override
    public Function<Path, Stream<String>> lines() {
        return path -> {
            try {
                return Files.readAllLines(path).stream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return empty();
        };
    }

    @Override
    public Logger log() {
        return LOGGER;
    }
}