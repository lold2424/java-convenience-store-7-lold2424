package store.view;

import store.message.IOMessage;
import store.domain.product.Product;

import java.util.List;

public class StoreView {
    public void displayProducts(List<Product> products) {
        System.out.println(IOMessage.START_MESSAGE.getMessage());
        products.forEach(System.out::println);
    }

    public void displayError(String errorMessage) {
        System.out.println(errorMessage);
    }
}
