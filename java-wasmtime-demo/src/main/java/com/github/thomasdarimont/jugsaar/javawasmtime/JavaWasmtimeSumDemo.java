package com.github.thomasdarimont.jugsaar.javawasmtime;

import io.github.kawamuray.wasmtime.Instance;
import io.github.kawamuray.wasmtime.Module;
import io.github.kawamuray.wasmtime.Store;
import io.github.kawamuray.wasmtime.Val;
import io.github.kawamuray.wasmtime.WasmValType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class JavaWasmtimeSumDemo {

    private static final Logger LOG = LoggerFactory.getLogger(JavaWasmtimeSumDemo.class);

    public static void main(String[] args) {

        try (var store = Store.withoutData(); //
             var engine = store.engine(); //
             var module = Module.fromFile(engine, WasmIO.locateWatFromClasspath("sum.wat").toFile().getAbsolutePath()); //
             var instance = new Instance(store, module, Collections.emptyList()); //
             var func = instance.getFunc(store, "calc").orElseThrow()) {

            var results = func.call(store, WasmValType.I32.toWasmVal(3), WasmValType.I32.toWasmVal(4));

            var result = (Val) results[0];

            LOG.info("Result: {}", result.i32());
        }
    }
}