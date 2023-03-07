package exitsmdemo;

import org.extism.sdk.Context;
import org.extism.sdk.manifest.Manifest;
import org.extism.sdk.wasm.WasmSourceResolver;

import java.nio.file.Path;

public class ExtismExample {

    public static void main(String[] args) {

        var manifest = new Manifest(new WasmSourceResolver().resolve(Path.of("code.wasm")));

        try (var ctx = new Context(); //
             var plugin = ctx.newPlugin(manifest, false)) {

            var output = plugin.call("count_vowels", "Hello World");
            System.out.println(output);
        }

    }
}