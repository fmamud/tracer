# tracer

Log parser generator to JSON trees.

```
[start-timestamp] [end-timestamp] [trace] [service-name] [caller-span]->[span]
```

# Usage

```
Usage: tracer [options]
Options:
  -i, --input=<file>, --stdin      log file input
  -o, --output=<file>, --stdout    trace file output
  -e, --engine=<inmemory|file>     select engine to separating and processing traces (default: inmemory)
```

> If log file input is a huge file or you don't have available RAM memory, you can use `--engine=file`.

First you **must** build the project and **run** executable jar:

```sh
./gradlew build
java -jar build/libs/tracer.jar <options>
```

You can also to run with Gradle:

```sh
./gradlew run -Ptrace.args="<option>"
```