package org.landsreyk.sorter;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LargeCsvSorter {

    public static final int BLOCK_SIZE = 5000;
    private final String tempDir;
    private final String inputFile;
    private final String outputFile;

    /**
     * Создаём новый сортировщик файлов в формате CSV
     *
     * @param workDir  - рабочая директория
     * @param fileName - имя исходного файла в рабочей директории
     */
    public LargeCsvSorter(String workDir, String fileName) {
        tempDir = workDir + "temp/";
        outputFile = workDir + "result.csv";
        inputFile = workDir + fileName;
    }

    @SneakyThrows
    public void sort() {
        List<String> fileSegments = splitFile();
        fileSegments.parallelStream().forEach(this::sortSegment);
        String result;
        if (fileSegments.size() < 2) {
            result = fileSegments.get(0);
        } else {
            result = fileSegments.parallelStream().reduce(this::mergeSegments).orElseThrow();
        }
        deleteFile(outputFile);
        Files.move(Paths.get(result), Paths.get(outputFile));
    }

    @SneakyThrows
    private List<String> splitFile() {
        new File(tempDir).mkdir();
        List<String> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line = reader.readLine();
            String segmentName;
            PrintWriter writer = null;
            for (int count = 0, i = 1; line != null; count++) {
                if (count == BLOCK_SIZE) {
                    count = 0;
                    i++;
                }
                if (count == 0) {
                    segmentName = tempDir + "segment_" + i + ".csv";
                    result.add(segmentName);
                    writer = new PrintWriter(segmentName);
                }
                writer.println(line);
                writer.flush();
                line = reader.readLine();
            }
            if (writer != null) {
                writer.close();
            }
        }

        return result;
    }

    @SneakyThrows
    private void sortSegment(String fileSegment) {
        List<String> lines = readLines(fileSegment);
        lines.sort(Comparator.comparing(this::extractKey));
        String content = String.join(System.lineSeparator(), lines);
        Files.writeString(Paths.get(fileSegment), content);
    }

    @SneakyThrows
    private String mergeSegments(String file1, String file2) {
        File tempFile = Files.createTempFile(Paths.get(tempDir), "temp", null).toFile();
        try (
                BufferedReader reader1 = new BufferedReader(new FileReader(file1));
                BufferedReader reader2 = new BufferedReader(new FileReader(file2));
                PrintWriter writer = new PrintWriter(tempFile)
        ) {
            String line1 = reader1.readLine();
            String line2 = reader2.readLine();

            while (line1 != null || line2 != null) {
                if (line2 == null) {
                    writer.println(line1);
                    line1 = reader1.readLine();
                    continue;
                }
                if (line1 == null) {
                    writer.println(line2);
                    line2 = reader2.readLine();
                    continue;
                }
                if (extractKey(line1) <= extractKey(line2)) {
                    writer.println(line1);
                    line1 = reader1.readLine();
                } else {
                    writer.println(line2);
                    line2 = reader2.readLine();
                }
            }
            writer.flush();
        }
        deleteFile(file1);
        deleteFile(file2);

        return tempFile.getAbsolutePath();
    }

    private boolean deleteFile(String file) {
        return new File(file).delete();
    }

    @SneakyThrows
    private List<String> readLines(String file) {
        return Files.readAllLines(Paths.get(file));
    }

    private int extractKey(String line) {
        return Integer.parseInt(line.split(",")[0]);
    }
}