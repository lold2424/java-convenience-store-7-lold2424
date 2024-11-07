package store.model;

public class Product {
    private String name;
    private int price;
    private int quantity;
    private String promotion;

    public Product(String[] data) {
        if (data.length < 4) {
            throw new IllegalArgumentException("[ERROR] Invalid product data format: " + String.join(",", data));
        }

        this.name = data[0].trim();

        try {
            this.price = Integer.parseInt(data[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] Price is not a valid number: " + String.join(",", data));
        }

        try {
            this.quantity = Integer.parseInt(data[2].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] Quantity is not a valid number: " + String.join(",", data));
        }

        this.promotion = data[3].trim().equals("null") ? null : data[3].trim();
    }

    @Override
    public String toString() {
        return "- " + name + " " + price + "원 " + quantity + "개 " + promotion;
    }
}
