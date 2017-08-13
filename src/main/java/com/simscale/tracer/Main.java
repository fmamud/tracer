package com.simscale.tracer;

import com.simscale.tracer.ast.JSONTree;
import com.simscale.tracer.ast.LogLine;
import com.simscale.tracer.ast.Node;
import com.simscale.tracer.parser.ArgumentsParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Main {
    public static void main(String[] args) throws Exception {
        ArgumentsParser opts = ArgumentsParser.parse(args);

        long startTime = System.nanoTime();

        Path path = Paths.get("/Users/felipe/Downloads/SimScale_Backend_Case_2/medium-log.txt");

        try (Stream<String> lines = Files.lines(path)) {
            lines.parallel().forEach(line -> {
                LogLine logLine = new LogLine(line.split("\\s+"));
                File file = new File("/tmp/sim", String.format("%s.txt", logLine.trace));
                file.deleteOnExit();
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true), 65536)) {
                    bw.write(line);
                    bw.write('\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((System.nanoTime() - startTime), TimeUnit.NANOSECONDS);
        System.out.println("Write files ok: " + elapsedTimeInMillis + " ms");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File("/Users/felipe/Downloads/SimScale_Backend_Case_2/my-log.txt"), true), 65536)) {
            Files.list(Paths.get("/tmp/sim"))
                    .map(Main::generateTrace)
                    .forEach(tree -> {
                        try {
                            bw.write(tree.toString());
                            bw.write('\n');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }

        elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((System.nanoTime() - startTime), TimeUnit.NANOSECONDS);
        System.out.println("Total elapsed time: " + elapsedTimeInMillis + " ms");
    }

    private static JSONTree generateTrace(Path path) {
        JSONTree tree = new JSONTree();
        try {
            tree.id = path.getFileName().toString().replace(".txt", "");

            Map<String, List<LogLine>> pathMap = Files.readAllLines(path)
                    .stream()
                    .parallel()
                    .map(line -> new LogLine(line.split("\\s+")))
                    .collect(Collectors.groupingBy(LogLine::getCallerSpan));

            LogLine ll = pathMap.get("null").get(0);

            Node root = new Node(ll.start, ll.end, ll.serviceName, ll.span);
            tree.root = root;
            walk(pathMap, root);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tree;
    }

    public static List<Node> walk(Map<String, List<LogLine>> pathMap, Node node) {
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