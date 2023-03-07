package com.github.thomasdarimont.jugsaar.javawasmtime;

import io.github.kawamuray.wasmtime.Engine;
import io.github.kawamuray.wasmtime.Extern;
import io.github.kawamuray.wasmtime.Func;
import io.github.kawamuray.wasmtime.Instance;
import io.github.kawamuray.wasmtime.Module;
import io.github.kawamuray.wasmtime.Store;
import io.github.kawamuray.wasmtime.WasmFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Example for calling a supplied callback function from a wasm module.
 */
public class JavaWasmtimeHelloDemo {

    private static final Logger LOG = LoggerFactory.getLogger(JavaWasmtimeHelloDemo.class);

    public static void main(String[] args) {

        // Creating the global datastore `Store`.
        // Note, it is also possible to tweak additional configuration settings with a `Config` and custom `Engine`.
        LOG.info("Initializing...");
        try (Store<Void> store = Store.withoutData()) {

            // Compile the wasm binary into an in-memory instance of a `Module`.
            LOG.info("Compiling module...");
            try (Engine engine = store.engine();
                 // .wasm: Web Assembly binary format
//                 Module module = Module.fromBinary(engine, WasmIO.loadWasmFromClasspath("hello.wasm"))
                 // .wat: Web Assembly Text Format
                  Module module = Module.fromFile(engine, WasmIO.locateWatFromClasspath("hello.wat").toFile().getAbsolutePath())
            ) {
                // We want to expose a custom function callback to the wasm module
                LOG.info("Creating callback...");
                WasmFunctions.Consumer0 helloFuncCallback = () -> {
                    // note that this callback takes no parameters and produces no results
                    LOG.info("Inside callback...");
                    // custom code here
                    LOG.info("Hello World!");
                };

                try (Func helloFuncToImport = WasmFunctions.wrap(store, helloFuncCallback)) {

                    // Here we handle the imports of the module, which in this case is our
                    // `HelloCallback` type and its associated implementation of a `Callback.
                    var imports = List.of(Extern.fromFunc(helloFuncToImport));

                    // Next come the instantiation phase: This combines the compiled module with the given imports.
                    // Note, that this is where the wasm `start` function, if any, would run.
                    LOG.info("Instantiating module...");
                    try (var instance = new Instance(store, module, imports)) {

                        // Next we poke around a bit to extract the `run` function from the module.
                        LOG.info("Extracting exported function...");
                        try (var func = instance.getFunc(store, "run").orElseThrow()) {
                            WasmFunctions.Consumer0 funcCaller = WasmFunctions.consumer(store, func);

                            // now invoke the run function
                            LOG.info("Calling exported function...");
                            funcCaller.accept();

                            LOG.info("Done.");
                        }
                    }
                }
            }
        }
    }
}
