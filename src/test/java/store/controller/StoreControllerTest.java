package store.controller;

import store.helper.TestHelper;

public class StoreControllerTest {
    public static void main(String[] args) {
        StoreControllerTest tester = new StoreControllerTest();
        tester.testMainFlow();
        System.out.println("StoreController 테스트를 통과했습니다.");
    }

    private final TestHelper helper = new TestHelper();

    private void testMainFlow() {
        helper.setUpOutputCapture();

        String simulatedInput = "[콜라-2]\nN\nN\n";
        helper.provideInput(simulatedInput);

        StoreController.main(new String[]{});

        String output = helper.getOutput();

        assert output.contains("안녕하세요. W편의점입니다.");
        assert output.contains("구매하실 상품명과 수량을 입력해 주세요.");
        assert output.contains("멤버십 할인을 받으시겠습니까? (Y/N)");
        assert output.contains("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        assert output.contains("내실돈");

        helper.tearDown();
    }
}
