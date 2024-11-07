package store.service;

import store.model.Product;
import store.model.Promotion;
import store.util.MarkdownReader;

import java.util.List;
import java.util.stream.Collectors;

public class StockSituationService {
    private final MarkdownReader reader = new MarkdownReader();

    public List<Product> loadProducts(String filename) {
        List<String[]> rawData = reader.readMarkdownFile(filename);
        return rawData.stream()
                .map(data -> {
                    try {
                        return new Product(data);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(product -> product != null)
                .collect(Collectors.toList());
    }

    public List<Promotion> loadPromotions(String filename) {
        List<String[]> rawData = reader.readMarkdownFile(filename);
        return rawData.stream()
                .map(data -> {
                    try {
                        return new Promotion(data);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(promotion -> promotion != null)
                .collect(Collectors.toList());
    }
}
