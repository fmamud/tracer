package com.simscale.tracer.ast;

public class LogLine {
    public final static LogLine NULL_CALLER_SPAN = new LogLine("null");

    public final static LogLine EOF = new LogLine("EOF");

    public String start, end, trace, serviceName, callerSpan, span;

    private LogLine(String spans) {
        this.span = spans;
        this.callerSpan = spans;
    }

    public LogLine(String[] line) {
        start = line[0];
        end = line[1];
        trace = line[2];
        serviceName = line[3];
        String[] spanNode = line[4].split("->");
        callerSpan = spanNode[0];
        span = spanNode[1];
    }

    @Override
    public String toString() {
        return "LogLine{" +
                "start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", trace='" + trace + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", callerSpan='" + callerSpan + '\'' +
                ", span='" + span + '\'' +
                '}';
    }

    public String getCallerSpan() {
        return callerSpan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogLine logLine = (LogLine) o;

        if (start != null ? !start.equals(logLine.start) : logLine.start != null) return false;
        if (end != null ? !end.equals(logLine.end) : logLine.end != null) return false;
        if (trace != null ? !trace.equals(logLine.trace) : logLine.trace != null) return false;
        if (serviceName != null ? !serviceName.equals(logLine.serviceName) : logLine.serviceName != null) return false;
        if (callerSpan != null ? !callerSpan.equals(logLine.callerSpan) : logLine.callerSpan != null) return false;
        return span != null ? span.equals(logLine.span) : logLine.span == null;
    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = 31 * result + (end != null ? end.hashCode() : 0);
        result = 31 * result + (trace != null ? trace.hashCode() : 0);
        result = 31 * result + (serviceName != null ? serviceName.hashCode() : 0);
        result = 31 * result + (callerSpan != null ? callerSpan.hashCode() : 0);
        result = 31 * result + (span != null ? span.hashCode() : 0);
        return result;
    }
}
