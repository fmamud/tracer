package com.simscale.tracer.parser;

import org.junit.Test;

import java.io.FileNotFoundException;

public class ArgumentsParserTest {

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyArsg() throws FileNotFoundException {
        ArgumentsParser.parse(new String[0]);
    }

    @Test
    public void testValiArgs() throws FileNotFoundException {
        ArgumentsParser parse = ArgumentsParser.parse(new String[]{"--stdin", "file.txt", "-o", "--stdout"});
    }
}
