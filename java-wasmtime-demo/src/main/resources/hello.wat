(module
;; convert .wat to .wasm via `wat2wasm hello.wat
;; internal function "$hello" which we call with arguments "hello"
  (func $hello (import "" "hello"))
;; expose function "run" - which calls our imported function
  (func (export "run") (call $hello))
)
