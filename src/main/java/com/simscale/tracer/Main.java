package com.simscale.tracer;

import com.simscale.tracer.cmd.processing.InMemoryProcessor;
import com.simscale.tracer.cmd.Step;
import com.simscale.tracer.cmd.separation.InMemorySeparator;
import com.simscale.tracer.parser.ArgumentsParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static final Map<String, List<String>> data = new HashMap<>(5000);

    public static void main(String[] args) throws Exception {
        ArgumentsParser opts = ArgumentsParser.parse(args);

        Step.sequence(new InMemorySeparator(opts.getInput()),
                new InMemoryProcessor(opts.getOutput()));
    }
}