# tracer

Log parser generator to JSON trees.

```
[start-timestamp] [end-timestamp] [trace] [service-name] [caller-span]->[span]
```

 


# Usage

```
tracer [options] [trace-log.txt]
Options:

  -i, --input   <file>      log file input
  --stdin                   use standard input to log input
  -o, --output  <file>      trace file output
  --stdout                  use standard input to log input

```

First you **must** build the project and **run** executable jar:

```sh
./gradlew build
java -jar build/libs/tracer.jar <options>
```

You can also to run with Gradle:

```sh
./gradlew run -Ptrace.args="<option>"
```