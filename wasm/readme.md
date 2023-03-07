WASM Examples
----

# WAT with wasmtime

Execute exported function with wasmtime

```shell
wasmtime sum.wat --invoke calc 1 2
```

# JS to WASM

Use [javy](https://github.com/Shopify/javy) to convert JS to wasm.

```shell
javy compile code.js -o code.wasm
```

```shell
wasm2wat code.wasm
```

```shell
echo '{ "a": 2, "b": 3 }' | wasmtime code.wasm
```