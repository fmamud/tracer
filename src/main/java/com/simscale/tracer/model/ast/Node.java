package com.simscale.tracer.model.ast;

import java.util.List;

import static java.util.Collections.emptyList;

public final class Node {
    private final String start, end, service, span;

    public List<Node> calls = emptyList();

    public Node(String start, String end, String service, String span) {
        this.start = start;
        this.end = end;
        this.service = service;
        this.span = span;
    }

    public String getSpan() {
        return span;
    }

    @Override
    public String toString() {
        return String.format("{\"start\":\"%s\",\"end\":\"%s\",\"service\":\"%s\",\"span\":\"%s\",\"calls\":%s}", start, end, service, span, calls);
    }
}
