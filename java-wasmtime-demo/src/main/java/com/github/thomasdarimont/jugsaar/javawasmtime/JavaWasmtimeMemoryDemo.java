package com.github.thomasdarimont.jugsaar.javawasmtime;

import io.github.kawamuray.wasmtime.Engine;
import io.github.kawamuray.wasmtime.Extern;
import io.github.kawamuray.wasmtime.Func;
import io.github.kawamuray.wasmtime.FuncType;
import io.github.kawamuray.wasmtime.Instance;
import io.github.kawamuray.wasmtime.Linker;
import io.github.kawamuray.wasmtime.Module;
import io.github.kawamuray.wasmtime.Store;
import io.github.kawamuray.wasmtime.Val;
import io.github.kawamuray.wasmtime.WasmFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Example for calling a supplied callback function from a wasm module with parameter passing and reading data from wasm memory.
 */
public class JavaWasmtimeMemoryDemo {

    private static final Logger LOG = LoggerFactory.getLogger(JavaWasmtimeMemoryDemo.class);

    public static void main(String[] args) {

        LOG.info("Initializing...");
        try (Store<Void> store = Store.withoutData()) {

            LOG.info("Compiling module...");
            try (Engine engine = store.engine(); //
                 Module module = Module.fromBinary(engine, WasmIO.loadWasmFromClasspath("memory.wasm"))
//                 Module module = Module.fromFile(engine, WasmIO.locateWatFromClasspath("memory.wat").toFile().getAbsolutePath())
            ) {

                try (var linker = new Linker(engine)) {

                    LOG.info("Creating callback...");

                    // log function signature (address:i32, length:i32) Void
                    var logFuncType = FuncType.emptyResults(Val.Type.I32, Val.Type.I32);

                    // log function handler with caller context, given params and results
                    Func.Handler<Void> logFuncHandler = (caller, params, results) -> {

                        var mem = caller.getExport("mem").orElseThrow(() -> new RuntimeException("export not found: mem"));

                        var address = params[0].i32();
                        var length = params[1].i32();
                        var data = new byte[length];
                        try (var memory = mem.memory()) {
                            var byteBuffer = memory.buffer(store);
                            byteBuffer.get(data, address, length);
                        }
                        var message = new String(data);

                        LOG.info("message: {}", message);
                    };

                    var logFunc = Extern.fromFunc(new Func(store, logFuncType, logFuncHandler));
                    linker.define("", "log", logFunc);

                    LOG.info("Instantiating module...");
                    var imports = List.of(logFunc);

                    try (var instance = new Instance(store, module, imports)) {

                        LOG.info("Extracting export...");
                        try (var func = instance.getFunc(store, "run").orElseThrow()) {

                            LOG.info("Calling exported function...");
                            WasmFunctions.consumer(store, func).accept();

                            LOG.info("Done.");
                        }
                    }
                }
            }
        }
    }
}
