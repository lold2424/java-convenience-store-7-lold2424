package store.controller;

import store.domain.Product;
import store.domain.Promotion;
import store.service.ProductService;
import store.service.PromotionService;
import store.service.PurchaseService;
import store.util.DateProvider;
import store.util.SystemDateProvider;
import store.view.View;

import java.util.List;
import java.util.Map;

public class StoreController {

    public static void main(String[] args) {
        View view = new View();
        DateProvider dateProvider = new SystemDateProvider();
        PromotionService promotionService = new PromotionService(dateProvider);
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