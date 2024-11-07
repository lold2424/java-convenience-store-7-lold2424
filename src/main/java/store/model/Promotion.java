package store.model;

public class Promotion {
    private String name;
    private int buyQuantity;
    private int getQuantity;
    private String startDate;
    private String endDate;

    public Promotion(String[] data) {
        if (data.length < 5) {
            throw new IllegalArgumentException("[ERROR] Invalid promotion data format: " + String.join(",", data));
        }

        this.name = data[0].trim();

        try {
            this.buyQuantity = Integer.parseInt(data[1].trim());
            this.getQuantity = Integer.parseInt(data[2].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] Buy or get quantity is not a valid number: " + String.join(",", data));
        }

        this.startDate = data[3].trim();
        this.endDate = data[4].trim();
    }

    @Override
    public String toString() {
        return "Promotion{name='" + name + "', buyQuantity=" + buyQuantity + ", getQuantity=" + getQuantity +
                ", startDate='" + startDate + "', endDate='" + endDate + "'}";
    }
}
