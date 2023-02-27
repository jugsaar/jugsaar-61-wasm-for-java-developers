#include <stdio.h>

// compile c to wasm via emcc compiler
// emcc -o floyd.wasm floyd.c

// run compilied wasm directly on graalvm
// wasm --Builtins=wasi_snapshot_preview1 floyd.wasm

int main() {
  int number = 1;
  int rows = 10;
  for (int i = 1; i <= rows; i++) {
    for (int j = 1; j <= i; j++) {
      printf("%d ", number);
      ++number;
    }
    printf(".\n");
  }
  return 0;
}