package com.simscale.tracer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TestData {

    public static final List<String> TRACES =
            Stream.of("2013-10-23T10:12:35.298Z 2013-10-23T10:12:35.300Z eckakaau service3 d6m3shqy->62d45qeh",
                    "2013-10-23T10:12:35.293Z 2013-10-23T10:12:35.302Z eckakaau service7 zfjlsiev->d6m3shqy",
                    "2013-10-23T10:12:35.286Z 2013-10-23T10:12:35.302Z eckakaau service9 bm6il56t->zfjlsiev",
                    "2013-10-23T10:12:35.339Z 2013-10-23T10:12:35.339Z eckakaau service1 nhxtegwv->4zhimp35",
                    "2013-10-23T10:12:35.339Z 2013-10-23T10:12:35.342Z eckakaau service1 22buxmqp->nhxtegwv",
                    "2013-10-23T10:12:35.345Z 2013-10-23T10:12:35.361Z eckakaau service5 22buxmqp->3wos67cv",
                    "2013-10-23T10:12:35.318Z 2013-10-23T10:12:35.370Z eckakaau service3 bm6il56t->22buxmqp",
                    "2013-10-23T10:12:35.271Z 2013-10-23T10:12:35.471Z eckakaau service6 null->bm6il56t")
                    .collect(Collectors.toList());

    public static final String JSON_TREE = "{\"id\":\"eckakaau\",\"root\":{\"start\":\"2013-10-23T10:12:35.271Z\",\"end\":\"2013-10-23T10:12:35.471Z\",\"service\":\"service6\",\"span\":\"bm6il56t\",\"calls\":[{\"start\":\"2013-10-23T10:12:35.286Z\",\"end\":\"2013-10-23T10:12:35.302Z\",\"service\":\"service9\",\"span\":\"zfjlsiev\",\"calls\":[{\"start\":\"2013-10-23T10:12:35.293Z\",\"end\":\"2013-10-23T10:12:35.302Z\",\"service\":\"service7\",\"span\":\"d6m3shqy\",\"calls\":[{\"start\":\"2013-10-23T10:12:35.298Z\",\"end\":\"2013-10-23T10:12:35.300Z\",\"service\":\"service3\",\"span\":\"62d45qeh\",\"calls\":[]}]}]}, {\"start\":\"2013-10-23T10:12:35.318Z\",\"end\":\"2013-10-23T10:12:35.370Z\",\"service\":\"service3\",\"span\":\"22buxmqp\",\"calls\":[{\"start\":\"2013-10-23T10:12:35.339Z\",\"end\":\"2013-10-23T10:12:35.342Z\",\"service\":\"service1\",\"span\":\"nhxtegwv\",\"calls\":[{\"start\":\"2013-10-23T10:12:35.339Z\",\"end\":\"2013-10-23T10:12:35.339Z\",\"service\":\"service1\",\"span\":\"4zhimp35\",\"calls\":[]}]}, {\"start\":\"2013-10-23T10:12:35.345Z\",\"end\":\"2013-10-23T10:12:35.361Z\",\"service\":\"service5\",\"span\":\"3wos67cv\",\"calls\":[]}]}]}}";

    public static final String TRACE_ID = "eckakaau";

    public static final InputStream TRACES_INPUT = new ByteArrayInputStream(TRACES.stream().collect(Collectors.joining("\n")).getBytes());
}
