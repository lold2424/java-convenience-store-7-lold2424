package store.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MarkdownReader {

    public List<String[]> readMarkdownFile(String filename) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(filename)))) {

            if (br == null) {
                throw new IllegalStateException("[ERROR] File not found: " + filename);
            }

            br.readLine(); // 첫 줄(헤더) 건너뛰기
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line.split(","));
            }
        } catch (java.io.IOException e) {
            throw new IllegalStateException("[ERROR] Could not read file: " + filename);
        }
        return data;
    }
}
