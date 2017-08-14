package com.simscale.tracer.model;

public class LogLine {
    private final String start, end, trace, serviceName, callerSpan, span;

    public LogLine(String[] line) throws ArrayIndexOutOfBoundsException {
        start = line[0];
        end = line[1];
        trace = line[2];
        serviceName = line[3];
        String[] spanNode = line[4].split("->");
        callerSpan = spanNode[0];
        span = spanNode[1];
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getTrace() {
        return trace;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getSpan() {
        return span;
    }

    public String getCallerSpan() {
        return callerSpan;
    }
}
