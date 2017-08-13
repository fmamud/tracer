package com.simscale.tracer.cmd;

import com.simscale.tracer.ast.LogLine;

import java.io.*;

public class SeparatingFiles implements Step {
    private static final int BUFFER_SIZE = 1024 * 32;

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
                    File file = new File(System.getProperty("tracer.tmp.dir", "/tmp/sim"), logLine.trace);
                    file.deleteOnExit();
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true), BUFFER_SIZE)) {
                        bw.write(line);
                        bw.write('\n');
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.err.printf("ERROR: trace parser error -> %s\n", line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}