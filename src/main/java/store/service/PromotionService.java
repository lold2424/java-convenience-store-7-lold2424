package store.service;

import camp.nextstep.edu.missionutils.Console;
import store.domain.product.Product;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionHandler;
import store.model.PurchaseRequest;

import java.time.LocalDate;
import java.util.List;

public class PromotionService {
    private final PromotionHandler promotionHandler = new PromotionHandler();

    public void applyPromotions(List<PurchaseRequest> requests, List<Product> products, List<Promotion> promotions) {
        for (PurchaseRequest request : requests) {
            // 프로모션이 적용된 상품과 없는 상품 분리
            Product promotionalProduct = findProductWithPromotion(request.getProductName(), products);
            Product nonPromotionalProduct = findProductWithoutPromotion(request.getProductName(), products);

            // 프로모션 객체 찾기
            Promotion promotion = findPromotion(promotionalProduct, promotions);

            // 프로모션 처리
            if (promotion != null && isWithinPromotionPeriod(promotion)) {
                promotionHandler.applyPromotion(request, promotionalProduct, nonPromotionalProduct, promotion);
            } else {
                handleNonPromotionalProduct(request, nonPromotionalProduct);
            }
        }
    }

    private Product findProductWithPromotion(String productName, List<Product> products) {
        return products.stream()
                .filter(p -> p.getName().equals(productName) && p.getPromotion() != null)
                .findFirst()
                .orElse(null); // 프로모션 상품이 없을 수도 있음
    }

    private Product findProductWithoutPromotion(String productName, List<Product> products) {
        return products.stream()
                .filter(p -> p.getName().equals(productName) && p.getPromotion() == null)
                .findFirst()
                .orElse(null); // 비프로모션 상품이 없을 수도 있음
    }

    private Promotion findPromotion(Product product, List<Promotion> promotions) {
        if (product == null || product.getPromotion() == null) return null;

        return promotions.stream()
                .filter(p -> p.getName().equals(product.getPromotion()))
                .findFirst()
                .orElse(null);
    }

    private boolean isWithinPromotionPeriod(Promotion promotion) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = LocalDate.parse(promotion.getStartDate());
        LocalDate endDate = LocalDate.parse(promotion.getEndDate());
        return !now.isBefore(startDate) && !now.isAfter(endDate);
    }

    private void handleNonPromotionalProduct(PurchaseRequest request, Product product) {
        if (product == null) {
            System.out.printf("[ERROR] %s는 재고가 없습니다.%n", request.getProductName());
            return;
        }

        System.out.printf("현재 %s은(는) 프로모션 할인이 적용되지 않습니다.%n", product.getName());
        System.out.printf("%s %d개를 구매하시겠습니까? (Y/N)%n", product.getName(), request.getQuantity());

        while (true) {
            try {
                String input = Console.readLine().trim().toUpperCase();
                validateUserInput(input);
                if (input.equals("Y")) {
                    System.out.println("구매를 진행합니다.");
                    return;
                }
                System.out.println("구매를 취소했습니다.");
                return;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void validateUserInput(String input) {
        if (!input.equals("Y") && !input.equals("N")) {
            throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. Y 또는 N을 입력해 주세요.");
        }
    }
}
