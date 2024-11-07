package store;

import store.controller.StockSituationController;

public class Application {
    public static void main(String[] args) {
        StockSituationController controller = new StockSituationController();
        controller.start();
    }
}
