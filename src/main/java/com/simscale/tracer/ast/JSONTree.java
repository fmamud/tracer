package com.simscale.tracer.ast;

public class JSONTree {
    public String id;
    public Node root;

    @Override
    public String toString() {
        return String.format("{\"id\":\"%s\",\"root\":%s}", id, root);
    }
}
