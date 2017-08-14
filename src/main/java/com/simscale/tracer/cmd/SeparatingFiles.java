package com.simscale.tracer.cmd;

import com.simscale.tracer.model.LogLine;

import java.io.*;
import java.util.logging.Logger;

import static java.lang.String.format;

public class SeparatingFiles implements Step {
    private final static Logger LOGGER = Logger.getLogger(SeparatingFiles.class.getName());

    private InputStream input;

    public SeparatingFiles(InputStream input) {
        this.input = input;
    }

    @Override
    public void execute() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    LogLine logLine = new LogLine(line.split("\\s+"));
                    File file = new File(System.getProperty("tracer.tmp.dir", "/tmp/sim"), logLine.getTrace());
                    file.deleteOnExit();
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                        bw.write(line);
                        bw.write('\n');
                    } catch (IOException e) {
                        LOGGER.severe(e.getMessage());
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    LOGGER.warning(format("trace parser error -> %s", line));
                }
            }
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }
}