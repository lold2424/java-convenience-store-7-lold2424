package store.service;

import store.domain.Product;
import store.domain.Promotion;
import store.domain.PurchaseRequest;
import store.message.ErrorMessage;
import store.view.View;

import java.text.NumberFormat;
import java.util.*;

public class PurchaseService {

    private View view;

    public PurchaseService(View view) {
        this.view = view;
    }

    public void processPurchase(List<Product> products, Map<String, Promotion> promotions) {
        List<PurchaseRequest> purchaseRequests = new ArrayList<>();
        boolean purchaseComplete = false;
        while (!purchaseComplete) {
            try {
                executePurchase(products, promotions, purchaseRequests);
                purchaseComplete = true;
            } catch (IllegalArgumentException e) {
                view.showError(e.getMessage());
            }
        }
    }

    private void executePurchase(List<Product> products, Map<String, Promotion> promotions, List<PurchaseRequest> purchaseRequests) {
        String[] itemList = view.getPurchaseItems();
        List<PurchaseRequest> tempRequests = parsePurchaseItems(itemList, products, promotions);

        processPurchaseRequests(products, purchaseRequests, tempRequests);

        int membershipDiscount = applyMembershipDiscount(purchaseRequests);

        printReceipt(purchaseRequests, membershipDiscount);
    }

    private void processPurchaseRequests(List<Product> products, List<PurchaseRequest> purchaseRequests, List<PurchaseRequest> tempRequests) {
        for (PurchaseRequest request : tempRequests) {
            handlePurchaseRequest(products, request);
            purchaseRequests.add(request);
        }
    }

    private List<PurchaseRequest> parsePurchaseItems(String[] itemList, List<Product> products, Map<String, Promotion> promotions) {
        List<PurchaseRequest> tempRequests = new ArrayList<>();
        for (String item : itemList) {
            PurchaseRequest request = parsePurchaseItem(item);
            processPurchaseItem(request, products, promotions, tempRequests);
        }
        return tempRequests;
    }

    private PurchaseRequest parsePurchaseItem(String item) {
        if (!item.matches("\\[.+-\\d+\\]")) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT_FORMAT.getMessage(item));
        }

        String[] splitItem = item.replaceAll("[\\[\\]]", "").split("-");
        if (splitItem.length != 2) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT_FORMAT.getMessage(item));
        }

        String productName = splitItem[0].trim();
        int quantity;
        try {
            quantity = Integer.parseInt(splitItem[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.QUANTITY_NOT_NUMBER.getMessage(splitItem[1].trim()));
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException(ErrorMessage.QUANTITY_LESS_THAN_ONE.getMessage(quantity));
        }

        return new PurchaseRequest(productName, quantity, 0);
    }

    private void processPurchaseItem(PurchaseRequest request, List<Product> products, Map<String, Promotion> promotions, List<PurchaseRequest> tempRequests) {
        List<Product> matchedProducts = findProductsByName(products, request.getProductName());
        if (matchedProducts.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.PRODUCT_NOT_FOUND.getMessage(request.getProductName()));
        }

        int requestedQuantity = request.getQuantity();
        int totalStock = getTotalStock(matchedProducts);

        if (requestedQuantity > totalStock) {
            throw new IllegalArgumentException(ErrorMessage.STOCK_EXCEEDED.getMessage(requestedQuantity, totalStock, totalStock));
        }

        Product product = matchedProducts.get(0);
        Promotion promotion = promotions.get(product.getPromotion());

        boolean isPromotionActive = promotion != null && promotion.isActive();

        if (!isPromotionActive) {
            setNonPromotionalRequest(request, product, requestedQuantity);
            tempRequests.add(request);
        } else {
            int promoStock = product.getPromotionStock();
            int nonPromoStock = totalStock - promoStock;
            processPromotionAndAddRequests(tempRequests, request, promoStock, nonPromoStock, product, promotion);
        }
    }

    private int getTotalStock(List<Product> matchedProducts) {
        return matchedProducts.stream().mapToInt(Product::getStock).sum();
    }

    private void setNonPromotionalRequest(PurchaseRequest request, Product product, int requestedQuantity) {
        request.setPrice(product.getPrice());
        request.setPromotionApplicable(false);
        request.setFinalQuantity(requestedQuantity);
    }

    private void processPromotionAndAddRequests(
            List<PurchaseRequest> tempRequests,
            PurchaseRequest request,
            int promoStock,
            int nonPromoStock,
            Product product,
            Promotion promotion
    ) {
        int requestedQuantity = request.getQuantity();
        int promotionTotal = promotion.getBuy() + promotion.getGet();
        int maxPromotionCycles = promoStock / promotionTotal;
        int promoApplicableQuantity = Math.min(requestedQuantity, maxPromotionCycles * promotionTotal);
        int nonPromoApplicableQuantity = requestedQuantity - promoApplicableQuantity;

        if (promoApplicableQuantity < requestedQuantity) {
            int nonPromotionalQuantity = requestedQuantity - promoApplicableQuantity;
            handleNonPromoQuantity(nonPromotionalQuantity, product.getName());
        }

        int promoCycleRemainder = promoApplicableQuantity % promotionTotal;
        if (promoCycleRemainder >= promotion.getBuy() && promoCycleRemainder < promotionTotal) {
            promoApplicableQuantity = adjustPromoQuantityForAdditionalPurchase(
                    promoApplicableQuantity,
                    promoCycleRemainder,
                    promotionTotal,
                    promoStock,
                    requestedQuantity,
                    product.getName(),
                    promotion
            );
        }

        request.setPrice(product.getPrice());
        request.setPromotionApplicable(true);
        request.setPromotion(promotion);

        int fullPromotionCycles = promoApplicableQuantity / promotionTotal;
        int giftedQuantity = fullPromotionCycles * promotion.getGet();

        request.setGiftedQuantity(giftedQuantity);
        request.setFinalQuantity(promoApplicableQuantity + nonPromoApplicableQuantity);

        tempRequests.add(request);
    }

    private int adjustPromoQuantityForAdditionalPurchase(
            int promoApplicableQuantity,
            int promoCycleRemainder,
            int promotionTotal,
            int promoStock,
            int requestedQuantity,
            String productName,
            Promotion promotion
    ) {
        int additionalNeeded = promotionTotal - promoCycleRemainder;
        view.showPromotionSuggestion(productName, promotion.getGet());
        boolean userWantsAdditional = view.getUserConfirmation();
        if (!userWantsAdditional) {
            return promoApplicableQuantity;
        }
        if (promoApplicableQuantity + additionalNeeded <= promoStock) {
            promoApplicableQuantity += additionalNeeded;
            return promoApplicableQuantity;
        }
        int nonPromoQuantity = requestedQuantity - promoApplicableQuantity;
        handleNonPromoQuantity(nonPromoQuantity, productName);
        return promoApplicableQuantity;
    }

    private void handleNonPromoQuantity(int nonPromoQuantity, String productName) {
        view.showNonPromotionWarning(productName, nonPromoQuantity);
        boolean proceed = view.getUserConfirmation();
        if (!proceed) {
            throw new IllegalArgumentException("[ERROR] 구매가 취소되었습니다.");
        }
    }

    private List<Product> findProductsByName(List<Product> products, String productName) {
        List<Product> matchedProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                matchedProducts.add(product);
            }
        }

        matchedProducts.sort((p1, p2) -> {
            if (p1.getPromotion() == null && p2.getPromotion() != null) {
                return 1;
            }
            if (p1.getPromotion() != null && p2.getPromotion() == null) {
                return -1;
            }
            return 0;
        });

        return matchedProducts;
    }

    private void handlePurchaseRequest(List<Product> products, PurchaseRequest request) {
        String productName = request.getProductName();

        int totalQuantity = request.getFinalQuantity() + request.getGiftedQuantity();

        for (Product product : products) {
            if (!product.getName().equals(productName) || product.getStock() <= 0) {
                continue;
            }
            int reducible = Math.min(totalQuantity, product.getStock());
            product.reduceStock(reducible);
            totalQuantity -= reducible;
            if (totalQuantity == 0) {
                break;
            }
        }
    }

    private int applyMembershipDiscount(List<PurchaseRequest> purchaseRequests) {
        view.showMembershipPrompt();
        boolean applyDiscount = view.getUserConfirmation();

        if (!applyDiscount) {
            return 0;
        }

        int totalNonPromoPrice = calculateTotalNonPromoPrice(purchaseRequests);
        int membershipDiscount = (int) (totalNonPromoPrice * 0.3);
        if (membershipDiscount > 8000) {
            membershipDiscount = 8000;
        }
        return membershipDiscount;
    }

    private int calculateTotalNonPromoPrice(List<PurchaseRequest> purchaseRequests) {
        int totalNonPromoPrice = 0;
        for (PurchaseRequest request : purchaseRequests) {
            if (request.isPromotionApplicable()) {
                continue;
            }
            totalNonPromoPrice += request.getFinalQuantity() * request.getPrice();
        }
        return totalNonPromoPrice;
    }

    private void printReceipt(List<PurchaseRequest> purchaseRequests, int membershipDiscount) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        System.out.println("\n==============W 편의점================");
        System.out.println("상품명\t\t수량\t금액");

        int totalItems = 0;
        int totalAmount = 0;
        int totalDiscount = 0;

        for (PurchaseRequest request : purchaseRequests) {
            if (request.getFinalQuantity() <= 0) {
                continue;
            }
            int cost = request.getFinalQuantity() * request.getPrice();
            totalItems += request.getFinalQuantity();
            totalAmount += cost;
            System.out.println(request.getProductName() + "\t\t" + request.getFinalQuantity() + "\t" + numberFormat.format(cost));
        }

        System.out.println("=============증\t정===============");
        for (PurchaseRequest request : purchaseRequests) {
            if (request.getGiftedQuantity() > 0) {
                System.out.println(request.getProductName() + "\t\t" + request.getGiftedQuantity());
            }
        }

        for (PurchaseRequest request : purchaseRequests) {
            if (request.getGiftedQuantity() > 0) {
                int discount = request.getGiftedQuantity() * request.getPrice();
                totalDiscount += discount;
            }
        }

        int finalAmount = totalAmount - totalDiscount - membershipDiscount;

        System.out.println("====================================");
        System.out.println("총구매액\t\t" + totalItems + "\t" + numberFormat.format(totalAmount));
        System.out.println("행사할인\t\t\t-" + numberFormat.format(totalDiscount));
        if (membershipDiscount > 0) {
            System.out.println("멤버십할인\t\t\t-" + numberFormat.format(membershipDiscount));
        }
        System.out.println("내실돈\t\t\t " + numberFormat.format(finalAmount));
    }


}
