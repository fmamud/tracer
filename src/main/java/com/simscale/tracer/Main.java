package com.simscale.tracer;

import com.simscale.tracer.cmd.ProcessingFiles;
import com.simscale.tracer.cmd.SeparatingFiles;
import com.simscale.tracer.cmd.Step;
import com.simscale.tracer.parser.ArgumentsParser;

public class Main {
    public static void main(String[] args) throws Exception {
        ArgumentsParser opts = ArgumentsParser.parse(args);

        Step.sequence(new SeparatingFiles(opts.getInput()),
                new ProcessingFiles(opts.getOutput()));
    }
}