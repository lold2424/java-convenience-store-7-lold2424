package store.service;

import store.model.Product;
import store.model.Promotion;
import store.util.MarkdownReader;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StockSituationService {
    private final MarkdownReader reader = new MarkdownReader();

    private <T> List<T> loadEntities(String filename, String expectedHeader, Function<String[], T> mapper) {
        List<String[]> rawData = reader.readMarkdownFile(filename, expectedHeader);
        return rawData.stream()
                .map(data -> {
                    try {
                        return mapper.apply(data);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(entity -> entity != null)
                .collect(Collectors.toList());
    }

    public List<Product> loadProducts(String filename) {
        return loadEntities(filename, "name,price,quantity,promotion", Product::new);
    }

    public List<Promotion> loadPromotions(String filename) {
        return loadEntities(filename, "name,buy,get,start_date,end_date", Promotion::new);
    }
}
