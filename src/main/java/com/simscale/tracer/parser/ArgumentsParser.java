package com.simscale.tracer.parser;

import com.simscale.tracer.model.Engine;

import java.io.*;

import static com.simscale.tracer.model.Engine.INMEMORY;
import static java.lang.String.format;

public final class ArgumentsParser {
    private static final int MAX_ARGS = 5;

    private final InputStream input;
    private final OutputStream output;
    private final Engine engine;

    private final boolean exitOnHelp;

    private ArgumentsParser(InputStream input, OutputStream output, Engine engine, boolean exitOnHelp) throws IllegalArgumentException {
        if (input == null || output == null) {
            throw new IllegalArgumentException("input and output are required");
        }
        this.input = input;
        this.output = output;
        this.engine = engine;
        this.exitOnHelp = exitOnHelp;
    }

    public static ArgumentsParser parse(String[] args) throws FileNotFoundException, IllegalArgumentException {
        return parse(args, true);
    }

    public static ArgumentsParser parse(String[] args, boolean exitOnHelp) throws FileNotFoundException, IllegalArgumentException {
        InputStream input = null;
        OutputStream output = null;
        Engine engine = INMEMORY;

        try {
            if (args == null || args.length > MAX_ARGS)
                throw new IllegalArgumentException(format("args size must be less than or equal to %d", MAX_ARGS));

            for (int i = 0; i < args.length; i++) {
                if ("-i".equals(args[i]))
                    input = new FileInputStream(args[i + 1]);
                else if (args[i].startsWith("--input"))
                    input = new FileInputStream(args[i].split("=")[1]);
                else if ("--stdin".equals(args[i]))
                    input = System.in;

                if ("-o".equals(args[i]))
                    output = new FileOutputStream(args[i + 1], true);
                else if (args[i].startsWith("--output"))
                    output = new FileOutputStream(args[i].split("=")[1], true);
                else if ("--stdout".equals(args[i]))
                    output = System.out;

                if ("-e".equals(args[i]))
                    engine = Engine.of(args[i + 1]);
                else if (args[i].startsWith("--engine"))
                    engine = Engine.of(args[i].split("=")[1]);

                if ("-h".equals(args[i]) || "-help".equals(args[i]) || "--help".equals(args[i]))
                    help(exitOnHelp);
            }

        } catch (ArrayIndexOutOfBoundsException ex) {
            help(exitOnHelp);
        }
        return new ArgumentsParser(input, output, engine, exitOnHelp);
    }

    public InputStream input() {
        return input;
    }

    public OutputStream output() {
        return output;
    }

    public Engine engine() {
        return engine;
    }

    public static void help() {
        help(false);
    }

    public static void help(boolean exitOnHelp) {
        help(null, exitOnHelp);
    }

    public static void help(String message, boolean exitOnHelp) {
        if (message != null) System.err.println(message);
        System.out.println(HELP_TEXT);
        if (exitOnHelp) System.exit(1);
    }

    private static final String HELP_TEXT =
            "Usage: tracer [options]\n" +
                    "Options:\n" +
                    "  -i, --input=<file>, --stdin      log file input\n" +
                    "  -o, --output=<file>, --stdout    trace file output\n" +
                    "  -e, --engine=<inmemory|file>     select engine to separating and processing traces (default: inmemory)\n";
}