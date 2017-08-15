package com.simscale.tracer.cmd.processing;

import com.simscale.tracer.cmd.Step;
import com.simscale.tracer.model.Engine;
import com.simscale.tracer.model.LogLine;
import com.simscale.tracer.model.ast.Node;
import com.simscale.tracer.model.ast.NodeTree;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public interface Processable<T> extends Step {
    int BUFFER_SIZE = 1024 * 128;

    OutputStream stream();

    Stream<T> data();

    Function<T, String> id();

    Function<T, Stream<String>> lines();

    @Override
    default void execute() {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stream()), BUFFER_SIZE)) {
            data().map(this::generateTrace)
                    .filter(tree -> tree.getRoot() != null)
                    .forEach(tree -> this.write(bw, tree));
        } catch (IOException e) {
            log().severe(e.getMessage());
        }
    }

    default void write(BufferedWriter bw, NodeTree tree) {
        try {
            bw.write(tree.toString());
            bw.write('\n');
        } catch (IOException e) {
            log().severe(e.getMessage());
        }
    }

    default NodeTree generateTrace(T obj) {
        NodeTree tree = new NodeTree(id().apply(obj));
        try {
            Map<String, List<LogLine>> pathMap = lines().apply(obj)
                    .map(line -> new LogLine(line.split("\\s+")))
                    .collect(Collectors.groupingBy(LogLine::getCallerSpan));

            LogLine ll = ofNullable(pathMap.get("null")).map(list -> list.get(0)).orElseThrow(IllegalStateException::new);

            Node root = new Node(ll.getStart(), ll.getEnd(), ll.getServiceName(), ll.getSpan());
            tree.setRoot(root);
            walk(pathMap, root);
        } catch (IllegalStateException ex) {
            log().warning(format("trace does not have root -> %s\n", tree.getId()));
        }

        return tree;
    }

    default List<Node> walk(Map<String, List<LogLine>> pathMap, Node node) {
        List<LogLine> logLines = pathMap.get(node.getSpan());
        if (logLines != null) {
            node.calls = logLines.stream()
                    .parallel()
                    .map(ll -> {
                        Node n = new Node(ll.getStart(), ll.getEnd(), ll.getServiceName(), ll.getSpan());
                        n.calls = walk(pathMap, n);
                        return n;
                    }).collect(toList());
        }
        return node.calls;
    }

    static Processable create(Engine engine, OutputStream stream) {
        switch (engine) {
            case INMEMORY:
                return new InMemoryProcessor(stream);
            case FILE:
                return new FileProcessor(stream);
            default:
                throw new IllegalArgumentException(format("Engine '%s' does not Processable implementation", engine.name()));
        }
    }
}
