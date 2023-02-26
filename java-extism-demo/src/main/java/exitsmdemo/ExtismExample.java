package exitsmdemo;

import org.extism.sdk.Context;
import org.extism.sdk.manifest.Manifest;
import org.extism.sdk.wasm.WasmSourceResolver;

import java.nio.file.Path;

public class ExtismExample {

    public static void main(String[] args) {

        var wasmPath = "code.wasm";
        var functionName = "count_vowels";
        var input = "Hello World";

        var wasmSourceResolver = new WasmSourceResolver();
        var manifest = new Manifest(wasmSourceResolver.resolve(Path.of(wasmPath)));

        System.out.printf("Executing \"%s\" from \"%s\" with input \"%s\"%n", functionName, wasmPath, input);

        try (var ctx = new Context()) {
            try (var plugin = ctx.newPlugin(manifest, false)) {
                var output = plugin.call(functionName, input);
                System.out.println(output);
            }
        }
    }
}