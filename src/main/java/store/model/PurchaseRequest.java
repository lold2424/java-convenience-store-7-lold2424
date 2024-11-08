package store.model;

public class PurchaseRequest {
    private final String productName;
    private final int quantity;

    public PurchaseRequest(String productName, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("[ERROR] 수량은 0보다 커야 합니다: " + productName + "-" + quantity);
        }
        this.productName = productName.trim();
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "PurchaseRequest{" +
                "productName='" + productName + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
