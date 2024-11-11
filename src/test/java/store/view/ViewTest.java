package store.view;

import store.domain.Product;
import store.helper.TestHelper;

import java.util.Arrays;
import java.util.List;

public class ViewTest {
    public static void main(String[] args) {
        ViewTest tester = new ViewTest();
        tester.testDisplayProducts();
        tester.testGetPurchaseItems();
        tester.testShowMessages();
        System.out.println("모든 View 테스트를 통과했습니다.");
    }

    private final TestHelper helper = new TestHelper();

    private void testDisplayProducts() {
        helper.setUpOutputCapture();

        View view = new View();
        List<Product> products = Arrays.asList(
                new Product("콜라", 1000, 10, "탄산2+1"),
                new Product("사이다", 1000, 0, null)
        );
        view.displayProducts(products);

        String output = helper.getOutput();
        assert output.contains("안녕하세요. W편의점입니다.");
        assert output.contains("- 콜라 1,000원 10개 탄산2+1");
        assert output.contains("- 사이다 1,000원 재고 없음");

        helper.tearDown();
    }

    private void testGetPurchaseItems() {
        helper.setUpOutputCapture();
        String simulatedInput = "[콜라-2],[사이다-1]\n";
        helper.provideInput(simulatedInput);

        View view = new View();
        String[] items = view.getPurchaseItems();

        assert items.length == 2;
        assert items[0].equals("[콜라-2]");
        assert items[1].equals("[사이다-1]");

        helper.tearDown();
    }

    private void testShowMessages() {
        helper.setUpOutputCapture();
        helper.provideInput("Y\n");

        View view = new View();
        view.showMembershipPrompt();
        boolean confirmation = view.getUserConfirmation();

        assert confirmation;

        String output = helper.getOutput();
        assert output.contains("멤버십 할인을 받으시겠습니까? (Y/N)");

        helper.tearDown();
    }
}
