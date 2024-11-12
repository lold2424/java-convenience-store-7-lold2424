package store.service;

import store.domain.Promotion;
import store.message.ErrorMessage;
import store.util.DateProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromotionService {
    private final DateProvider dateProvider;

    public PromotionService(DateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }

    public Map<String, Promotion> initializePromotions() {
        String promotionsFilePath = "promotions.md";
        String expectedHeader = "name,buy,get,start_date,end_date";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(promotionsFilePath);
        if (inputStream == null) {
            throw new IllegalArgumentException(ErrorMessage.FILE_NOT_FOUND.getMessage() + promotionsFilePath);
        }

        List<String[]> promotionData = readMarkdownFile(inputStream, expectedHeader);
        return parsePromotionData(promotionData);
    }

    public Map<String, Promotion> initializePromotions(InputStream inputStream) {
        String expectedHeader = "name,buy,get,start_date,end_date";
        List<String[]> promotionData = readMarkdownFile(inputStream, expectedHeader);
        return parsePromotionData(promotionData);
    }

    private List<String[]> readMarkdownFile(InputStream inputStream, String expectedHeader) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String headerLine = reader.readLine();
            validateHeader(headerLine, expectedHeader, "InputStream");

            return readLines(reader);
        } catch (IOException e) {
            throw new IllegalArgumentException(ErrorMessage.FILE_READ_ERROR.getMessage() + "InputStream");
        }
    }

    void validateHeader(String headerLine, String expectedHeader, String filename) {
        if (headerLine == null || !headerLine.trim().equals(expectedHeader)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_HEADER.getMessage() + filename);
        }
    }

    List<String[]> readLines(BufferedReader reader) throws IOException {
        List<String[]> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line.split(","));
        }
        return lines;
    }

    Map<String, Promotion> parsePromotionData(List<String[]> promotionData) {
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
        LocalDate startDate = LocalDate.parse(parts[3]);
        LocalDate endDate = LocalDate.parse(parts[4]);
        return new Promotion(name, buy, get, startDate, endDate, dateProvider);
    }
}
