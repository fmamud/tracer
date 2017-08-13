package com.simscale.tracer.ast;

import java.util.List;

import static java.util.Collections.emptyList;

public class Node {
    public String start, end, service, span;

    public List<Node> calls = emptyList();

    public Node(String start, String end, String service, String span, List<Node> calls) {
        this(start, end, service, span);
        this.calls = calls;
    }

    public Node(String start, String end, String service, String span) {
        this.start = start;
        this.end = end;
        this.service = service;
        this.span = span;
    }

    @Override
    public String toString() {
        return String.format("{\"start\":\"%s\",\"end\":\"%s\",\"service\":\"%s\",\"span\":\"%s\",\"calls\":%s}", start, end, service, span, calls);
    }
}
