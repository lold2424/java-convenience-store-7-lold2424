package store.model;

public class Promotion {
    private String name;
    private int buyQuantity;
    private int getQuantity;
    private String startDate;
    private String endDate;

    public Promotion(String[] data) {
        if (data.length < 5) {
            throwFormatError(data);
        }

        this.name = data[0].trim();
        this.buyQuantity = parseToInt(data[1], "구매 수량", data);
        this.getQuantity = parseToInt(data[2], "증정 수량", data);
        this.startDate = data[3].trim();
        this.endDate = data[4].trim();
    }

    @Override
    public String toString() {
        return "Promotion{name='" + name + "', buyQuantity=" + buyQuantity + ", getQuantity=" + getQuantity +
                ", startDate='" + startDate + "', endDate='" + endDate + "'}";
    }

    private static void throwFormatError(String[] data) {
        throw new IllegalArgumentException("[ERROR] 잘못된 프로모션 데이터 형식: " + String.join(",", data));
    }

    private static int parseToInt(String value, String fieldName, String[] data) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] " + fieldName + "이(가) 유효한 숫자가 아닙니다: " + String.join(",", data));
        }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getBuyQuantity() { return buyQuantity; }
    public void setBuyQuantity(int buyQuantity) { this.buyQuantity = buyQuantity; }
    public int getGetQuantity() { return getQuantity; }
    public void setGetQuantity(int getQuantity) { this.getQuantity = getQuantity; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
}
