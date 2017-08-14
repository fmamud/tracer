package com.simscale.tracer.cmd.separation;

import com.simscale.tracer.cmd.processing.FileProcessor;

import java.io.*;
import java.util.logging.Logger;

import static com.simscale.tracer.model.TraceDirectory.TRACE_DIR;
import static java.lang.String.format;

public class FileSeparator implements Separable {
    private static final Logger LOGGER = Logger.getLogger(FileProcessor.class.getName());

    private InputStream input;

    public FileSeparator(InputStream input) {
        this.input = input;
    }

    @Override
    public void separate(String line) {
        File file = new File(TRACE_DIR, line.split("\\s+")[2]);
        file.deleteOnExit();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(line);
            bw.write('\n');
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException ex) {
            LOGGER.warning(format("trace parser error -> %s", line));
        }
    }

    @Override
    public InputStream stream() {
        return input;
    }
}