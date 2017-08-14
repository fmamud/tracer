package com.simscale.tracer.model.ast;

public final class NodeTree {
    private final String id;
    private Node root;

    public NodeTree(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    @Override
    public String toString() {
        return String.format("{\"id\":\"%s\",\"root\":%s}", id, root);
    }
}
