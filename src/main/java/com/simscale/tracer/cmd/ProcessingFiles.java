package com.simscale.tracer.cmd;

import com.simscale.tracer.ast.JSONTree;
import com.simscale.tracer.ast.LogLine;
import com.simscale.tracer.ast.Node;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class ProcessingFiles implements Step {
    private static final int BUFFER_SIZE = 1024 * 32;

    private OutputStream output;

    public ProcessingFiles(OutputStream output) {
        this.output = output;
    }

    @Override
    public void execute() {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output), BUFFER_SIZE)) {
            Files.list(Paths.get(System.getProperty("tracer.tmp.dir", "/tmp/sim")))
                    .map(this::generateTrace)
                    .filter(tree -> tree.root != null)
                    .forEach(tree -> {
                        try {
                            bw.write(tree.toString());
                            bw.write('\n');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONTree generateTrace(Path path) {
        JSONTree tree = new JSONTree();
        tree.id = path.getFileName().toString();
        try {
            Map<String, List<LogLine>> pathMap = Files.readAllLines(path)
                    .stream()
                    .parallel()
                    .map(line -> new LogLine(line.split("\\s+")))
                    .collect(Collectors.groupingBy(LogLine::getCallerSpan));

            LogLine ll = ofNullable(pathMap.get("null")).map(list -> list.get(0)).orElseThrow(IllegalStateException::new);

            Node root = new Node(ll.start, ll.end, ll.serviceName, ll.span);
            tree.root = root;
            walk(pathMap, root);
        } catch (IllegalStateException ex) {
            System.err.printf("ERROR: trace does not have root -> %s\n", tree.id);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tree;
    }

    private List<Node> walk(Map<String, List<LogLine>> pathMap, Node node) {
        List<LogLine> logLines = pathMap.get(node.span);
        if (logLines != null) {
            node.calls = logLines.stream()
                    .parallel()
                    .map(ll -> {
                        Node n = new Node(ll.start, ll.end, ll.serviceName, ll.span);
                        n.calls = walk(pathMap, n);
                        return n;
                    }).collect(toList());
        }
        return node.calls;
    }
}

//try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File("/Users/felipe/Downloads/SimScale_Backend_Case_2/my-log.txt"), true), BUFFER_SIZE)) {
//        Files.list(Paths.get("/tmp/sim"))
//        .map(Main::generateTrace)
//        .forEach(tree -> {
//        try {
//        bw.write(tree.toString());
//        bw.write('\n');
//        } catch (IOException e) {
//        e.printStackTrace();
//        }
//        });
//        } catch (IOException e) {
//        e.printStackTrace();
//        }