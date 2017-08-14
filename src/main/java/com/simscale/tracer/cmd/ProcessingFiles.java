package com.simscale.tracer.cmd;

import com.simscale.tracer.model.LogLine;
import com.simscale.tracer.model.ast.Node;
import com.simscale.tracer.model.ast.NodeTree;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class ProcessingFiles implements Step {
    private static final int BUFFER_SIZE = 1024 * 128;

    private OutputStream output;

    private final static Logger LOGGER = Logger.getLogger(ProcessingFiles.class.getName());

    public ProcessingFiles(OutputStream output) {
        this.output = output;
    }

    @Override
    public void execute() {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output), BUFFER_SIZE)) {
            Files.list(Paths.get(System.getProperty("tracer.tmp.dir", "/tmp/sim")))
                    .map(this::generateTrace)
                    .filter(tree -> tree.getRoot() != null)
                    .forEach(tree -> this.write(bw, tree));
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    private void write(BufferedWriter bw, NodeTree tree) {
        try {
            bw.write(tree.toString());
            bw.write('\n');
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    private NodeTree generateTrace(Path path) {
        NodeTree tree = new NodeTree(path.getFileName().toString());
        try {
            Map<String, List<LogLine>> pathMap = Files.readAllLines(path)
                    .stream()
                    .parallel()
                    .map(line -> new LogLine(line.split("\\s+")))
                    .collect(Collectors.groupingBy(LogLine::getCallerSpan));

            LogLine ll = ofNullable(pathMap.get("null")).map(list -> list.get(0)).orElseThrow(IllegalStateException::new);

            Node root = new Node(ll.getStart(), ll.getEnd(), ll.getServiceName(), ll.getSpan());
            tree.setRoot(root);
            walk(pathMap, root);
        } catch (IllegalStateException ex) {
            LOGGER.warning(format("trace does not have root -> %s\n", tree.getId()));
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }

        return tree;
    }

    private List<Node> walk(Map<String, List<LogLine>> pathMap, Node node) {
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
}