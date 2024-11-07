package store.controller;

import store.model.Product;
import store.service.StockSituationService;
import store.view.StoreView;

import java.util.List;

public class StockSituationController {
    private final StockSituationService service = new StockSituationService();
    private final StoreView view = new StoreView();

    public void start() {
        try {
            List<Product> products = service.loadProducts("products.md");

            view.displayProducts(products);
        } catch (IllegalArgumentException | IllegalStateException e) {
            view.displayError(e.getMessage());
        }
    }
}
