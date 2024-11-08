package store.domain.promotion;

import camp.nextstep.edu.missionutils.Console;
import store.domain.product.Product;
import store.message.PromotionMessage;
import store.model.PurchaseRequest;

public class PromotionHandler {
    public void applyPromotion(PurchaseRequest request, Product promotionalProduct, Product nonPromotionalProduct, Promotion promotion) {
        int promotionalItems = calculatePromotionalItems(request, promotionalProduct, promotion);
        int remainingItems = request.getQuantity() - promotionalItems;

        if (processExtraPromotions(remainingItems, promotionalProduct, promotion)) return;

        if (remainingItems > 0) {
            processNonPromotionalItems(remainingItems, promotionalProduct, nonPromotionalProduct);
        }
    }

    private int calculatePromotionalItems(PurchaseRequest request, Product product, Promotion promotion) {
        int promotionUnit = promotion.getBuyQuantity() + promotion.getGetQuantity();
        int applicableUnits = calculateApplicableUnits(request, product, promotionUnit);
        applyPromotionalDiscount(product, applicableUnits, promotionUnit);
        return applicableUnits * promotionUnit;
    }

    private int calculateApplicableUnits(PurchaseRequest request, Product product, int promotionUnit) {
        return Math.min(request.getQuantity() / promotionUnit, product.getQuantity() / promotionUnit);
    }

    private void applyPromotionalDiscount(Product product, int applicableUnits, int promotionUnit) {
        if (applicableUnits > 0) {
            int promotionalItems = applicableUnits * promotionUnit;
            product.setQuantity(product.getQuantity() - promotionalItems);
        }
    }

    private boolean processExtraPromotions(int remainingItems, Product product, Promotion promotion) {
        if (!canOfferExtraPromotion(remainingItems, product, promotion.getBuyQuantity())) return false;
        return handleExtraPromotion(product, promotion.getBuyQuantity(), promotion.getGetQuantity());
    }

    private boolean canOfferExtraPromotion(int remainingItems, Product product, int buyQuantity) {
        return remainingItems >= buyQuantity && product.getQuantity() >= buyQuantity;
    }

    private boolean handleExtraPromotion(Product product, int buyQuantity, int getQuantity) {
        System.out.println(PromotionMessage.EXTRA_FREE_ITEMS_OFFER.format(product.getName(), getQuantity));
        String input = getUserInput();
        if (input.equals("Y")) {
            product.setQuantity(product.getQuantity() - buyQuantity);
        }
        return true;
    }

    private void processNonPromotionalItems(int remainingItems, Product promotionalProduct, Product nonPromotionalProduct) {
        System.out.println(PromotionMessage.NON_PROMOTIONAL_ITEMS.format(promotionalProduct.getName(), remainingItems));
        handleNonPromotionalPurchase(remainingItems, nonPromotionalProduct);
    }

    private void handleNonPromotionalPurchase(int remainingItems, Product nonPromotionalProduct) {
        String input = getUserInput();
        if (input.equals("Y")) {
            deductNonPromotionalStock(remainingItems, nonPromotionalProduct);
            System.out.println(PromotionMessage.NON_PROMOTIONAL_ITEMS_PURCHASED);
        } else {
            System.out.println(PromotionMessage.NON_PROMOTIONAL_ITEMS_CANCELED);
        }
    }

    private void deductNonPromotionalStock(int remainingItems, Product nonPromotionalProduct) {
        if (nonPromotionalProduct != null) {
            nonPromotionalProduct.setQuantity(nonPromotionalProduct.getQuantity() - remainingItems);
        }
    }

    private String getUserInput() {
        try {
            String input = Console.readLine().trim().toUpperCase();
            validateUserInput(input);
            return input;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return getUserInput();
        }
    }

    private void validateUserInput(String input) {
        if (!input.equals("Y") && !input.equals("N")) {
            System.out.println(PromotionMessage.INVALID_INPUT);
        }
    }
}
