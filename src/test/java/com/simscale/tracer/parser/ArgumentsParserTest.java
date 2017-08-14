package com.simscale.tracer.parser;

import org.junit.Test;

import java.io.*;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

public class ArgumentsParserTest {

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyArsg() throws FileNotFoundException {
        ArgumentsParser.parse(new String[0], false);
    }

    @Test
    public void testStandardArgs() throws FileNotFoundException {
        ArgumentsParser args = ArgumentsParser.parse(new String[]{"--stdin", "--stdout"}, false);

        assertSame(System.in, args.getInput());
        assertSame(System.out, args.getOutput());
    }

    @Test
    public void testFilesArgs() throws IOException {
        File inputPath = File.createTempFile("trace", "input.txt"),
                outputPath = File.createTempFile("trace", "output.txt");

        ArgumentsParser args = ArgumentsParser.parse(new String[]{"-i", inputPath.getAbsolutePath(), "-o", outputPath.getAbsolutePath()}, false);

        assertThat(args.getInput(), instanceOf(FileInputStream.class));
        assertThat(args.getOutput(), instanceOf(FileOutputStream.class));
    }

    @Test
    public void testFilesAndStandardArgs() throws IOException {
        File inputPath = File.createTempFile("trace", "input.txt");
        ArgumentsParser args = ArgumentsParser.parse(new String[]{"-i", inputPath.getAbsolutePath(), "--stdout"}, false);

        assertThat(args.getInput(), instanceOf(FileInputStream.class));
        assertSame(System.out, args.getOutput());
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotFound() throws IOException {
        File outputPath = File.createTempFile("trace", "output.txt");
        ArgumentsParser.parse(new String[]{"-i", "notFoundFile.txt", "-o", outputPath.getAbsolutePath()}, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArgs() throws IOException {
        ArgumentsParser.parse(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOverflowArgs() throws IOException {
        ArgumentsParser.parse(new String[]{"--stdin", "--stdout", "--stdin", "--stdout", "--stdin", "--stdout", "--stdin", "--stdout"});
    }
}
