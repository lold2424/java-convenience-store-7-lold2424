package store.domain;

public class PurchaseRequest {
    private final String productName;
    private int quantity;
    private int price;
    private int giftedQuantity;
    private int finalQuantity;

    private boolean isPromotionApplicable;
    private Promotion promotion;

    public PurchaseRequest(String productName, int quantity, int price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.giftedQuantity = 0;
        this.finalQuantity = quantity;
        this.isPromotionApplicable = false;
        this.promotion = null;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public int getGiftedQuantity() {
        return giftedQuantity;
    }

    public void setGiftedQuantity(int giftedQuantity) {
        this.giftedQuantity = giftedQuantity;
    }

    public int getFinalQuantity() {
        return finalQuantity;
    }

    public void setFinalQuantity(int finalQuantity) {
        this.finalQuantity = finalQuantity;
    }

    public boolean isPromotionApplicable() {
        return isPromotionApplicable;
    }

    public void setPromotionApplicable(boolean promotionApplicable) {
        isPromotionApplicable = promotionApplicable;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
