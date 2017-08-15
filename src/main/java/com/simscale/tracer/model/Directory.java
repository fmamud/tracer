package com.simscale.tracer.model;

import java.io.File;
import java.nio.file.Path;
import java.util.logging.Logger;

import static java.lang.String.format;

public class Directory {
    private static final Logger LOGGER = Logger.getLogger(Directory.class.getName());

    private static final String TRACE_DIR_PROPERTY = System.getProperty("trace.tmp.dir", "/tmp/traces");

    public static final File TRACE_DIR = file();

    public static final Path TRACE_PATH = TRACE_DIR.toPath();

    private static File file() {
        File file = new File(TRACE_DIR_PROPERTY);

        boolean created = file.mkdir();
        if (created) LOGGER.info(format("trace dir %s created", file.getAbsolutePath()));

        file.deleteOnExit();

        return file;
    }
}
