package com.simscale.tracer.cmd.separation;

import java.io.*;

import static com.simscale.tracer.model.TraceDirectory.TRACE_DIR;

public class FileSeparator implements Separable {
    private InputStream input;

    public FileSeparator(InputStream input) {
        this.input = input;
    }

    @Override
    public void separate(String line) throws IOException {
        File file = new File(TRACE_DIR, line.split("\\s+")[2]);
        file.deleteOnExit();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(line);
            bw.write('\n');
        }
    }

    @Override
    public InputStream stream() {
        return input;
    }
}