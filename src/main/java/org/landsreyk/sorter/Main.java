package org.landsreyk.sorter;

import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main {
    public static final String WORK_DIR_PATH = "D:/OneDrive/SORTED/STUDY/practice/csv-file-sorter/src/main/resources/";

    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Measurement(iterations = 5)
    public void testMethod() {
        LargeCsvSorter sorter = new LargeCsvSorter(WORK_DIR_PATH, "input_large.csv");
        sorter.sort();
    }
}
