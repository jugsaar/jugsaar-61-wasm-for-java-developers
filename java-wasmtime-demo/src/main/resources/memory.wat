(module

  ;; define custom parameter type (address and length)
  (type $t0 (func (param i32 i32)))

  ;; imported log function with the given parameter signaturs
  (import "" "log" (func $.log (type $t0)))

  ;; exported memory contents
  (memory (export "mem") 1 2)

  ;; store string literal in memory at pos 0 with (implict) length 11
  (data (i32.const 0) "Hello World!!!")
  (func $run
    i32.const 0
    i32.const 12
    call $.log
  )

  ;; exported "run" function to be called from outside
  (export "run" (func $run))
)