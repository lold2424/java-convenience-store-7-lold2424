package store.domain;

public class PurchaseRequestTest {
    public static void main(String[] args) {
        PurchaseRequestTest tester = new PurchaseRequestTest();
        tester.testPurchaseRequest();
        System.out.println("모든 PurchaseRequest 테스트를 통과했습니다.");
    }

    private void testPurchaseRequest() {
        PurchaseRequest request = createPurchaseRequest("콜라", 5, 1000);
        assertEquals("콜라", request.getProductName());
        assertEquals(5, request.getQuantity());
        assertEquals(1000, request.getPrice());
        assertEquals(0, request.getGiftedQuantity());
        assertEquals(5, request.getFinalQuantity());
        assertFalse(request.isPromotionApplicable());

        request.setGiftedQuantity(2);
        assertEquals(2, request.getGiftedQuantity());

        request.setFinalQuantity(7);
        assertEquals(7, request.getFinalQuantity());

        request.setPromotionApplicable(true);
        assertTrue(request.isPromotionApplicable());

        Promotion promotion = new Promotion("탄산2+1", 2, 1, null, null, null);
        request.setPromotion(promotion);
        assertEquals(promotion, request.getPromotion());
    }

    private PurchaseRequest createPurchaseRequest(String productName, int quantity, int price) {
        return new PurchaseRequest(productName, quantity, price);
    }

    private void assertEquals(Object expected, Object actual) {
        if ((expected == null && actual != null) || (expected != null && !expected.equals(actual))) {
            throw new AssertionError("기대 값: " + expected + ", 실제 값: " + actual);
        }
    }

    private void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("조건이 참이 아님");
        }
    }

    private void assertFalse(boolean condition) {
        if (condition) {
            throw new AssertionError("조건이 거짓이 아님");
        }
    }
}
