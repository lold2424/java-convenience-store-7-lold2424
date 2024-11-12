package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.domain.Product;
import store.message.ErrorMessage;
import store.message.ViewMessage;

import java.util.List;

public class View {

    public void displayProducts(List<Product> products) {
        System.out.println(ViewMessage.WELCOME.getMessage());
        for (Product product : products) {
            product.display();
        }
    }

    public String[] getPurchaseItems() {
        System.out.println(ViewMessage.ENTER_PURCHASE_ITEMS.getMessage());
        String items = Console.readLine();
        return items.split(",");
    }

    public void showPromotionSuggestion(String productName, int getAmount) {
        System.out.println(ViewMessage.PROMOTION_SUGGESTION.getMessage(productName, getAmount));
    }

    public void showNonPromotionWarning(String productName, int nonPromoQuantity) {
        System.out.println(ViewMessage.NON_PROMO_WARNING.getMessage(productName, nonPromoQuantity));
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
            System.out.println(ErrorMessage.INVALID_YN_INPUT.getMessage());
        }
    }

    public void showMembershipPrompt() {
        System.out.println(ViewMessage.MEMBERSHIP_PROMPT.getMessage());
    }

    public void showError(String message) {
        System.out.println(message);
    }

    public boolean askForMoreShopping() {
        System.out.println(ViewMessage.THANK_YOU.getMessage());
        while (true) {
            String input = Console.readLine().trim().toUpperCase();
            if ("Y".equals(input)) {
                return true;
            }
            if ("N".equals(input)) {
                return false;
            }
            System.out.println(ErrorMessage.INVALID_INPUT.getMessage());
        }
    }
}
