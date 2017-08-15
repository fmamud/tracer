# tracer

Log parser generator to JSON trees.

```
[start-timestamp] [end-timestamp] [trace] [service-name] [caller-span]->[span]
```

A trace execution has three distinct phases.

* **Initialization**: parsing args and preparing [Separable](https://github.com/fmamud/tracer/blob/master/src/main/java/com/simscale/tracer/cmd/separation/Separable.java) and [Processable](https://github.com/fmamud/tracer/blob/master/src/main/java/com/simscale/tracer/cmd/processing/Processable.java) implementations.
* **Separation**: reads log trace input and stores in data structure. (inmemory or file)
* **Processing**: process traces and generates JSON trees in output. (file or stdout)

# Usage

```
Usage: tracer [options]
Options:
  -i, --input=<file>, --stdin      log file input
  -o, --output=<file>, --stdout    trace file output
  -e, --engine=<inmemory|file>     select engine to separating and processing traces (default: inmemory)
  -s, --statistics                 show statistics (orphans, malformed, totals)
```

If log file input is a huge file or you don't have available RAM memory, you can use `--engine=file`. To processing with this engine, the tracer uses a temporary dir to creating trace log files, you can override using Java property `trace.tmp.dir`. (default: `/tmp/traces`)

# How to run

To run with **Gradle**:

```sh
./gradlew run -Ptrace.args="<option>"
```

You can also run with a **executable jar**:

```sh
./gradlew build
java -jar build/libs/tracer.jar <options>
```

