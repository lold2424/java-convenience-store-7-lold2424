package store.service;

import store.domain.Product;
import store.domain.Promotion;
import store.helper.TestHelper;
import store.view.View;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseServiceTest {
    public static void main(String[] args) {
        PurchaseServiceTest tester = new PurchaseServiceTest();
        tester.testProcessPurchase();
        System.out.println("모든 PurchaseService 테스트를 통과했습니다.");
    }

    private final TestHelper helper = new TestHelper();

    private void testProcessPurchase() {
        helper.setUpOutputCapture();

        View view = new View();
        PurchaseService purchaseService = new PurchaseService(view);

        List<Product> products = Arrays.asList(
                new Product("콜라", 1000, 10, null),
                new Product("사이다", 1000, 5, "탄산2+1")
        );

        Map<String, Promotion> promotions = new HashMap<>();
        promotions.put("탄산2+1", new Promotion("탄산2+1", 2, 1, null, null, null));

        String simulatedInput = "[콜라-2],[사이다-3]\nN\nY\n";
        helper.provideInput(simulatedInput);

        purchaseService.processPurchase(products, promotions);

        String output = helper.getOutput();

        assert output.contains("멤버십 할인을 받으시겠습니까? (Y/N)");
        assert output.contains("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        assert output.contains("내실돈");

        helper.tearDown();
    }
}
