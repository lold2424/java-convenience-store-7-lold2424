package store.domain.product;

import java.text.NumberFormat;
import java.util.Locale;

public class Product {
    private String name;
    private int price;
    private int quantity;
    private String promotion;

    public Product(String[] data) {
        if (data.length < 4) {
            throwFormatError(data);
        }

        this.name = data[0].trim();
        this.price = parseToInt(data[1], "가격", data);
        this.quantity = parseToInt(data[2], "수량", data);
        this.promotion = "null".equals(data[3].trim()) ? null : data[3].trim();
    }

    private String formatPrice() {
        return NumberFormat.getNumberInstance(Locale.KOREA).format(price) + "원";
    }

    @Override
    public String toString() {
        return "- " + name + " " + formatPrice() + " " + quantity + "개" + (promotion != null ? " " + promotion : "");
    }

    private static void throwFormatError(String[] data) {
        throw new IllegalArgumentException("[ERROR] 잘못된 제품 데이터 형식: " + String.join(",", data));
    }

    private static int parseToInt(String value, String fieldName, String[] data) {
        try {
            int parsedValue = Integer.parseInt(value.trim());
            if ("수량".equals(fieldName) && parsedValue < 0) {
                throw new IllegalArgumentException("[ERROR] 수량은 음수가 될 수 없습니다: " + String.join(",", data));
            }
            return parsedValue;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] " + fieldName + "이(가) 유효한 숫자가 아닙니다: " + String.join(",", data));
        }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getPromotion() { return promotion; }
    public void setPromotion(String promotion) { this.promotion = promotion; }
}
