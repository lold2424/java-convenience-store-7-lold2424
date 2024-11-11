package store.service;

import store.domain.Promotion;
import store.message.ErrorMessage;
import store.util.DateProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(parts[3], formatter);
        LocalDate endDate = LocalDate.parse(parts[4], formatter);
        return new Promotion(name, buy, get, startDate, endDate, dateProvider);
    }

    private List<String[]> readMarkdownFile(String filename, String expectedHeader) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(filename)))) {

            if (reader == null) {
                throw new IllegalArgumentException(ErrorMessage.FILE_NOT_FOUND.getMessage() + filename);
            }
            validateHeader(reader.readLine(), expectedHeader, filename);
            return readLines(reader);

        } catch (IOException e) {
            throw new IllegalArgumentException(ErrorMessage.FILE_READ_ERROR.getMessage() + filename);
        }
    }

    private void validateHeader(String headerLine, String expectedHeader, String filename) {
        if (headerLine == null || !headerLine.trim().equals(expectedHeader)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_HEADER.getMessage() + filename);
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
