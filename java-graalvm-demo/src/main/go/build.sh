#!/usr/bin/env bash


# needs tinygo
# needs binaryen

tinygo build -o ../../../prime.wasm -target wasi -no-debug -opt=z main.go