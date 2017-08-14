package com.simscale.tracer.parser;

import java.io.*;

public final class ArgumentsParser {

    private final InputStream input;
    private final OutputStream output;

    private final boolean exitOnHelp;

    private ArgumentsParser(InputStream input, OutputStream output, boolean exitOnHelp) throws IllegalArgumentException {
        if (input == null || output == null) {
            printHelp();
            throw new IllegalArgumentException("input or output not found");
        }
        this.input = input;
        this.output = output;
        this.exitOnHelp = exitOnHelp;
    }

    public static ArgumentsParser parse(String[] args) throws FileNotFoundException, IllegalArgumentException {
        return parse(args, true);
    }

    public static ArgumentsParser parse(String[] args, boolean exitOnHelp) throws FileNotFoundException, IllegalArgumentException {
        InputStream input = null;
        OutputStream output = null;

        if (args == null || args.length > 4) throw new IllegalArgumentException("args size must be less than or equal to 4");

        for (int i = 0; i < args.length; i++) {
            if ("-i".equals(args[i]) || "--input".equals(args[i]))
                try {
                    input = new FileInputStream(args[i + 1]);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    printHelp();
                }

            else if ("--stdin".equals(args[i]))
                input = System.in;

            if ("-o".equals(args[i]) || "--output".equals(args[i]))
                try {
                    output = new FileOutputStream(args[i + 1], true);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    printHelp();
                }

            else if ("--stdout".equals(args[i]))
                output = System.out;
        }

        return new ArgumentsParser(input, output, exitOnHelp);
    }

    public InputStream getInput() {
        return input;
    }

    public OutputStream getOutput() {
        return output;
    }

    public void help(String text) {
        System.err.println(String.format("ERROR: %s\n", text));
        printHelp();
        if (exitOnHelp) System.exit(1);
    }

    public static void printHelp() {
        System.out.println(HELP_TEXT);
    }

    private static final String HELP_TEXT =
            "Usage: java -jar tracer.jar [options] [trace-log.txt]\n" +
                    "Options:\n" +
                    "  -i, --input   <file>      log file input\n" +
                    "  --stdin                   use standard input to log input\n" +
                    "  -o, --output  <file>      trace file output\n" +
                    "  --stdout                  use standard input to log input\n";
}