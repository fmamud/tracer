package com.simscale.tracer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

public final class Database {
    private static final Map<String, List<String>> DATA = new HashMap<>(5000);

    public static List<String> get(String key) {
        return DATA.get(key);
    }

    public static void put(String key, String line) {
        List<String> lines = ofNullable(get(key)).orElse(new ArrayList<>());
        lines.add(line);
        DATA.put(key, lines);
    }

    public static Stream<Map.Entry<String, List<String>>> stream() {
        return DATA.entrySet().stream();
    }
}
