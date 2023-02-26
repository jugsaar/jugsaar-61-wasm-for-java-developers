## Running WebAssembly in GraalVM

Requires GraalVM 23.0.0 with WebAssembly module.

# Setup
```bash
sdk use java 23.0.0.r19-grl
$JAVA_HOME/bin/gu install wasm
```

# Run
```bash
javac Prime.java
java Prime
```