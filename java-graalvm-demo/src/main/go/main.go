package main

import (
	"fmt"
	"math/big"
	"os"
)

func main() {
	number := new(big.Int)
	_, err := fmt.Sscan(os.Args[1], number)
	if err != nil {
		fmt.Println("error scanning value: " + err.Error())
	} else {
	    // see https://pkg.go.dev/math/big#Int.ProbablyPrime
		if number.ProbablyPrime(10) {
			fmt.Println(os.Args[1] + " is probably prime\n")
		} else {
			fmt.Println(os.Args[1] + " is not prime\n")
		}
	}
}
