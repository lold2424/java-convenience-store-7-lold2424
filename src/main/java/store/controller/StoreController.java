package store.controller;

import store.domain.Product;
import store.domain.Promotion;
import store.service.ProductService;
import store.service.PromotionService;
import store.service.PurchaseService;
import store.view.View;

import java.util.List;
import java.util.Map;

public class StoreController {

    public static void main(String[] args) {
        View view = new View();
        PromotionService promotionService = new PromotionService();
        Map<String, Promotion> promotions = promotionService.initializePromotions();

        ProductService productService = new ProductService();
        List<Product> products = productService.initializeProducts(promotions);

        PurchaseService purchaseService = new PurchaseService(view);

        boolean shopping = true;
        while (shopping) {
            view.displayProducts(products);
            purchaseService.processPurchase(products, promotions);
            shopping = view.askForMoreShopping();
        }
    }
}
