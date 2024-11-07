package store.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MarkdownReader {

    public List<String[]> readMarkdownFile(String filename, String expectedHeader) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(filename)))) {

            if (reader == null) throw new IllegalStateException("[ERROR] 파일을 찾을 수 없습니다.: " + filename);
            validateHeader(reader.readLine(), expectedHeader, filename);
            return readLines(reader);

        } catch (java.io.IOException e) {
            throw new IllegalStateException("[ERROR] 파일을 읽을 수 없습니다.: " + filename, e);
        }
    }

    private void validateHeader(String header, String expectedHeader, String filename) {
        if (header == null || !header.trim().equals(expectedHeader)) {
            throw new IllegalArgumentException("[ERROR] 파일의 header가 없습니다. " + filename + ": " + header);
        }
    }

    private List<String[]> readLines(BufferedReader reader) throws java.io.IOException {
        List<String[]> data = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            data.add(line.split(","));
        }
        return data;
    }
}
