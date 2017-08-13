package com.simscale.tracer.parser;

import java.io.*;

public final class ArgumentsParser {

    private final InputStream input;
    private final OutputStream output;

    private ArgumentsParser(InputStream input, OutputStream output) {
        if (input == null || output == null) help("input or output cannot be null");
        this.input = input;
        this.output = output;
    }

    public static ArgumentsParser parse(String[] args) throws FileNotFoundException, IllegalArgumentException {
        InputStream input = null;
        OutputStream output = null;

        for (int i = 0; i < args.length; i++) {
            if ("-i".equals(args[i]) || "--input".equals(args[i]))
                try {
                    input = new FileInputStream(args[i + 1]);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    help("incorrect file input");
                }

            else if ("--stdin".equals(args[i]))
                input = System.in;

            if ("-o".equals(args[i]) || "--output".equals(args[i]))
                try {
                    output = new FileOutputStream(args[i + 1], true);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    help("incorrect file output");
                }

            else if ("--stdout".equals(args[i]))
                output = System.out;
        }

        return new ArgumentsParser(input, output);
    }

    public InputStream getInput() {
        return input;
    }

    public OutputStream getOutput() {
        return output;
    }

    public static void help(String text) {
        System.err.println(String.format("ERROR: %s", text));
        System.out.println(HELP_TEXT);
        System.exit(1);
    }

    private static final String HELP_TEXT =
            "Usage: java -jar tracer.jar [options] [trace-log.txt]\n" +
                    "Options:\n" +
                    "  -i, --input   <file>      log file input\n" +
                    "  --stdin                   use standard input to log input\n" +
                    "  -o, --output  <file>      trace file output\n" +
                    "  --stdout                  use standard input to log input\n";
}