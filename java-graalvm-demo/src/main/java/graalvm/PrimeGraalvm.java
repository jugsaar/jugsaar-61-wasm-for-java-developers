package graalvm;

import java.io.File;
import org.graalvm.polyglot.*;

public class PrimeGraalvm {

    public static void main(String[] args) throws Exception {

        String arg = "" + Integer.MAX_VALUE;

        File file = new File("prime.wasm");
        Source.Builder sourceBuilder = Source.newBuilder("wasm", file);
        Source source = sourceBuilder.build();

        Context.Builder contextBuilder = Context.newBuilder("wasm")//
                .option("wasm.Builtins", "wasi_snapshot_preview1").//
                arguments("wasm", new String[]{"prime.wasm", arg});

        try (Context context = contextBuilder.build()) {
            context.eval(source);

            Value mainFunction = context
                    .getBindings("wasm")
                    .getMember("main")
                    .getMember("_start");
            mainFunction.execute();
        } 
    }
}
