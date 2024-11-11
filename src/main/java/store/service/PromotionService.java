package store.service;

import store.domain.Promotion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class PromotionService {

    public Map<String, Promotion> initializePromotions() {
        String promotionsFilePath = "promotions.md";
        String expectedHeader = "name,buy,get,start_date,end_date";

        List<String[]> promotionData = readMarkdownFile(promotionsFilePath, expectedHeader);
        return parsePromotionData(promotionData);
    }

    private Map<String, Promotion> parsePromotionData(List<String[]> promotionData) {
        Map<String, Promotion> promotions = new HashMap<>();
        for (String[] parts : promotionData) {
            Promotion promotion = parsePromotion(parts);
            promotions.put(promotion.getName(), promotion);
        }
        return promotions;
    }

    private Promotion parsePromotion(String[] parts) {
        String name = parts[0];
        int buy = Integer.parseInt(parts[1]);
        int get = Integer.parseInt(parts[2]);
        // Ignore start_date and end_date for now
        return new Promotion(name, buy, get);
    }

    private List<String[]> readMarkdownFile(String filename, String expectedHeader) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(filename)))) {

            if (reader == null) {
                throw new IllegalArgumentException("[ERROR] 파일을 찾을 수 없습니다.: " + filename);
            }
            validateHeader(reader.readLine(), expectedHeader, filename);
            return readLines(reader);

        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] 파일을 읽을 수 없습니다.: " + filename);
        }
    }

    private void validateHeader(String headerLine, String expectedHeader, String filename) {
        if (headerLine == null || !headerLine.trim().equals(expectedHeader)) {
            throw new IllegalArgumentException("[ERROR] 파일 헤더가 예상과 다릅니다. 파일명: " + filename);
        }
    }

    private List<String[]> readLines(BufferedReader reader) throws IOException {
        List<String[]> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line.split(","));
        }
        return lines;
    }
}
