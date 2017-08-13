package com.simscale.tracer.parser;

import com.simscale.tracer.ast.LogLine;

import java.util.Iterator;
import java.util.List;

public class LogLineIterator implements Iterator<LogLine> {

    private final List<LogLine> list;

    private String nextCallerSpan = "null";

    public LogLineIterator(List<LogLine> list) {
        this.list = list;
    }

    @Override
    public boolean hasNext() {
        return !LogLine.EOF.callerSpan.equals(nextCallerSpan) && list.size() > 0;
    }

    @Override
    public LogLine next() {
        LogLine logLine = list.stream()
                .filter(ll -> ll.callerSpan.equals(nextCallerSpan))
                .findFirst()
                .orElse(LogLine.EOF);
        nextCallerSpan = logLine.span;
        list.remove(logLine);
        return logLine;
    }
}
