;; convert .wat to .wasm via `wat2wasm hello.wat
(module

;; internal function "$hello" which we call with no arguments
  (func $hello (import "" ""))

;; expose function "run" - which calls our imported function
  (func (export "run") (call $hello))
)
