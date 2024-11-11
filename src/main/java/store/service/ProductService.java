package store.service;

import store.domain.Product;
import store.domain.Promotion;
import store.message.ErrorMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ProductService {

    public List<Product> initializeProducts(Map<String, Promotion> promotions) {
        String productsFilePath = "products.md";
        String expectedHeader = "name,price,quantity,promotion";

        List<String[]> productData = readMarkdownFile(productsFilePath, expectedHeader);
        Map<String, List<Product>> groupedProducts = parseAndGroupProducts(productData, promotions);

        List<Product> products = new ArrayList<>();
        for (Map.Entry<String, List<Product>> entry : groupedProducts.entrySet()) {
            String name = entry.getKey();
            List<Product> productGroup = entry.getValue();

            products.addAll(productGroup);

            if (!hasNonPromotionalProduct(productGroup)) {
                Product lastProduct = productGroup.get(0);
                products.add(createNonPromotionalProduct(name, lastProduct));
            }
        }
        return products;
    }

    private Map<String, List<Product>> parseAndGroupProducts(List<String[]> productData, Map<String, Promotion> promotions) {
        Map<String, List<Product>> groupedProducts = new LinkedHashMap<>();
        for (String[] parts : productData) {
            Product product = parseProduct(parts, promotions);
            groupedProducts.computeIfAbsent(product.getName(), k -> new ArrayList<>()).add(product);
        }
        return groupedProducts;
    }

    private Product parseProduct(String[] parts, Map<String, Promotion> promotions) {
        String name = parts[0];
        int price = Integer.parseInt(parts[1]);
        int stock = Integer.parseInt(parts[2]);
        String promotion = (parts.length > 3 && !parts[3].equalsIgnoreCase("null")) ? parts[3] : null;
        if (promotion != null && !promotions.containsKey(promotion)) {
            throw new IllegalArgumentException("[ERROR] Promotion " + promotion + " not found for product " + name);
        }
        return new Product(name, price, stock, promotion);
    }

    private boolean hasNonPromotionalProduct(List<Product> productGroup) {
        return productGroup.stream().anyMatch(p -> p.getPromotion() == null);
    }

    // Updated method: Set stock to zero for non-promotional products created when no non-promotional stock exists
    private Product createNonPromotionalProduct(String name, Product lastProduct) {
        return new Product(name, lastProduct.getPrice(), 0, null);
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
