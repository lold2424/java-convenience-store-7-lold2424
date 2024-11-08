package store.service;

import store.message.InputErrorMessage;
import store.domain.product.Product;
import store.domain.promotion.Promotion;
import store.model.PurchaseRequest;
import store.util.MarkdownReader;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StockSituationService {
    private final MarkdownReader reader = new MarkdownReader();

    private <T> List<T> loadEntities(String filename, String expectedHeader, Function<String[], T> mapper) {
        return reader.readMarkdownFile(filename, expectedHeader)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    public List<Product> loadProducts(String filename) {
        return loadEntities(filename, "name,price,quantity,promotion", Product::new);
    }

    public List<Promotion> loadPromotions(String filename) {
        return loadEntities(filename, "name,buy,get,start_date,end_date", Promotion::new);
    }

    public List<PurchaseRequest> processPurchaseRequest(String input) {
        return List.of(input.split(",")).stream()
                .map(this::parsePurchaseItem)
                .collect(Collectors.toList());
    }

    private PurchaseRequest parsePurchaseItem(String item) {
        if (!item.matches("\\[.+-.+\\]")) {
            throw new IllegalArgumentException(InputErrorMessage.INVALID_FORMAT.getMessage());
        }

        String[] splitItem = item.replaceAll("[\\[\\]]", "").split("-");
        String productName = splitItem[0].trim();
        int quantity = parseQuantity(splitItem[1], item);

        return new PurchaseRequest(productName, quantity);
    }

    private int parseQuantity(String quantityStr, String item) {
        try {
            return Integer.parseInt(quantityStr.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(InputErrorMessage.INVALID_QUANTITY.getMessage() + ": " + item);
        }
    }
}
