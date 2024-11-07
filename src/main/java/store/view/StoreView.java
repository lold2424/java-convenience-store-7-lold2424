package store.view;

import store.model.Product;

import java.util.List;

public class StoreView {
    public void displayProducts(List<Product> products) {
        System.out.println("Loaded Products:");
        products.forEach(System.out::println);
    }

    public void displayError(String errorMessage) {
        System.out.println(errorMessage);
    }
}

