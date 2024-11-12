package store.domain;

import java.text.NumberFormat;

public class Product {
    private final String name;
    private final int price;
    private int stock;
    private final String promotion;
    private int promotionStock;

    public Product(String name, int price, int stock, String promotion) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotion = promotion;
        this.promotionStock = 0;
        if (promotion != null) {
            this.promotionStock = stock;
        }
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getPromotion() {
        return promotion;
    }

    public int getPromotionStock() {
        return promotionStock;
    }

    public void reduceStock(int quantity) {
        if (stock < quantity) {
            throw new IllegalArgumentException(name + "의 재고가 부족합니다.");
        }
        stock -= quantity;
        if (promotion != null) {
            int promoReducible = Math.min(quantity, promotionStock);
            promotionStock -= promoReducible;
        }
    }

    public void display() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        String promoString = "";
        if (promotion != null) {
            promoString = " " + promotion;
        }

        if (stock == 0) {
            System.out.println("- " + name + " " + numberFormat.format(price) + "원 재고 없음" + promoString);
            return;
        }
        System.out.println("- " + name + " " + numberFormat.format(price) + "원 " + stock + "개" + promoString);
    }
}
