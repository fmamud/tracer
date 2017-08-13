package com.simscale.tracer.parser;

import java.io.*;
import java.util.function.Supplier;

public final class ArgumentsParser {

    private final InputStream input;
    private final OutputStream output;

    private ArgumentsParser(InputStream input, OutputStream output) {
        this.input = input;
        this.output = output;
    }

    public static ArgumentsParser parse(String[] args) throws FileNotFoundException, IllegalArgumentException {
        InputStream input = System.in;
        OutputStream output = System.out;

        if (args.length == 4) {
            if ("-i".equals(args[0]) || "--input".equals(args[0]))
                input = new FileInputStream(args[1]);
            else
                help(IllegalArgumentException::new);

            if ("-o".equals(args[2]) || "--output".equals(args[2]))
                output = new FileOutputStream(args[3]);
            else if ("--stdout".equals(args[2]))
                output = System.out;
            else
                help(IllegalArgumentException::new);
        } else {
            help(IllegalArgumentException::new);
        }
        return new ArgumentsParser(input, output);
    }

    public InputStream getInput() {
        return input;
    }

    public OutputStream getOutput() {
        return output;
    }

    public static void help(Supplier<? extends RuntimeException> exception) {
        System.out.println(HELP_TEXT);
        throw exception.get();
    }

    private static final String HELP_TEXT =
            "Usage: java -jar tracer.jar [options] [trace-log.txt]\n" +
                    "Options:\n" +
                    "  -i, --input   <file>      log file input\n" +
                    "  --stdin                   use standard input to log input\n" +
                    "  -o, --output  <file>      trace file output\n" +
                    "  --stdout                  use standard input to log input\n";
}