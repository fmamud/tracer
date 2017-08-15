package com.simscale.tracer;

import com.simscale.tracer.cmd.Step;
import com.simscale.tracer.cmd.processing.Processable;
import com.simscale.tracer.cmd.separation.Separable;
import com.simscale.tracer.parser.ArgumentsParser;

import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            ArgumentsParser opts = ArgumentsParser.parse(args);

            Step.sequence(Separable.create(opts.engine(), opts.input()),
                    Processable.create(opts.engine(), opts.output()));
        } catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
            ArgumentsParser.help();
        }
    }
}