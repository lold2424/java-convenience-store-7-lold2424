package store.controller;

import store.domain.product.Product;
import store.domain.promotion.Promotion;
import store.model.PurchaseRequest;
import store.service.PromotionService;
import store.service.StockSituationService;
import store.view.InputView;
import store.view.StoreView;

import java.util.List;

public class StockSituationController {
    private final StockSituationService stockService = new StockSituationService();
    private final PromotionService promotionService = new PromotionService();
    private final StoreView view = new StoreView();
    private final InputView inputView = new InputView();

    public void start() {
        try {
            List<Product> products = stockService.loadProducts("products.md");
            List<Promotion> promotions = stockService.loadPromotions("promotions.md");

            view.displayProducts(products);
            List<PurchaseRequest> purchaseRequests = handleInput();

            promotionService.applyPromotions(purchaseRequests, products, promotions);

            purchaseRequests.forEach(System.out::println);

        } catch (IllegalArgumentException e) {
            view.displayError(e.getMessage());
        }
    }

    private List<PurchaseRequest> handleInput() {
        while (true) {
            try {
                String input = inputView.readPurchaseRequest();
                return stockService.processPurchaseRequest(input);
            } catch (IllegalArgumentException e) {
                view.displayError(e.getMessage());
            }
        }
    }
}
