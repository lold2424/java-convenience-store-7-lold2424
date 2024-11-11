package store.domain;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;

    public Promotion(String name, int buy, int get) {
        this.name = name;
        this.buy = buy;
        this.get = get;
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }
}
