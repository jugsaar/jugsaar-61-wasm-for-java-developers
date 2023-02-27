package graalvm;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.io.ByteSequence;

import java.nio.file.Files;
import java.nio.file.Path;

public class FloydGraalvm {

    public static void main(String[] args) throws Exception {

        //Load the WASM contents into a byte array
        byte[] binary = Files.readAllBytes(Path.of("floyd.wasm"));
        Source.Builder sourceBuilder = Source.newBuilder("wasm", ByteSequence.create(binary), "floyd");
        Source source = sourceBuilder.build();


        Context context = Context.newBuilder("wasm") //
                .option("wasm.Builtins", "wasi_snapshot_preview1") //
                .build();

        context.eval(source);

        Value mainFunction = context.getBindings("wasm")
                .getMember("main")
                .getMember("_start");

        try {
            mainFunction.execute();
        } catch (Exception ex) {
            System.out.println("Execution completed.");
        }
    }
}
