package demo;

import de.inetsoftware.jwebassembly.api.annotation.Export;
import de.inetsoftware.jwebassembly.api.annotation.Import;

public class Calculator {

    @Export
    public static int add(int a, int b) {
        return a + b;
    }

    @Import(module = "global.Math", name = "max")
    static int max(int a, int b) {
        return Math.max(a, b);
    }
}
