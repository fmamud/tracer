package com.simscale.tracer;

import com.simscale.tracer.ast.JSONTree;
import com.simscale.tracer.ast.LogLine;
import com.simscale.tracer.ast.Node;
import com.simscale.tracer.parser.ArgumentsParser;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Main {
    public static void main(String[] args) throws Exception {
        ArgumentsParser opts = ArgumentsParser.parse(args);

        Files.list(Paths.get("/tmp/sim"))
                .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        long start = System.currentTimeMillis();

        ExecutorService pool = Executors.newFixedThreadPool(10);

        try (Scanner sc = new Scanner(opts.getInput())) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                LogLine logLine = new LogLine(line.split("\\s+"));
                Path path = Paths.get("/tmp/sim", String.format("%s.txt", logLine.trace));

                try {
                    if (Files.notExists(path)) Files.createFile(path);
                    Files.write(path, line.getBytes(), StandardOpenOption.APPEND);
                    Files.write(path, new byte[]{'\n'}, StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Write files ok (" + (System.currentTimeMillis() - start) + "ms)");

        try (OutputStream sc = opts.getOutput()) {
            Files.list(Paths.get("/tmp/sim"))
                    .map(Main::generateTrace)
                    .forEach(tree -> {
                        try {
                            sc.write(tree.toString().getBytes());
                            sc.write('\n');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }

        pool.shutdown();

        System.out.println("time:" + (System.currentTimeMillis() - start) + "ms");
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