package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.domain.Product;

import java.util.List;

public class View {

    public void displayProducts(List<Product> products) {
        System.out.println("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n");
        for (Product product : products) {
            product.display();
        }
    }

    public String[] getPurchaseItems() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String items = Console.readLine();
        return items.split(",");
    }

    public void showPromotionSuggestion(String productName, int getAmount) {
        System.out.println("현재 " + productName + "은(는) " + getAmount + "개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
    }

    public void showNonPromotionWarning(String productName, int nonPromoQuantity) {
        System.out.println("현재 " + productName + " " + nonPromoQuantity + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
    }

    public boolean getUserConfirmation() {
        while (true) {
            String input = Console.readLine().trim().toUpperCase();
            if ("Y".equals(input)) {
                return true;
            }
            if ("N".equals(input)) {
                return false;
            }
            System.out.println("[ERROR] 잘못된 입력입니다. Y 또는 N을 입력하세요.");
        }
    }

    public void showMembershipPrompt() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
    }

    public void showError(String message) {
        System.out.println(message);
    }

    public boolean askForMoreShopping() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        while (true) {
            String input = Console.readLine().trim().toUpperCase();
            if ("Y".equals(input)) {
                return true;
            }
            if ("N".equals(input)) {
                return false;
            }
            System.out.println("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }
}
