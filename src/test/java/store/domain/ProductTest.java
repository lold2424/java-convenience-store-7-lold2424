package store.domain;

public class ProductTest {
    public static void main(String[] args) {
        ProductTest tester = new ProductTest();
        tester.testProductCreation();
        tester.testReduceStock();
        tester.testDisplay();
        System.out.println("모든 Product 테스트를 통과했습니다.");
    }

    private void testProductCreation() {
        Product product = createProduct("콜라", 1000, 10, "탄산2+1");
        assertEquals("콜라", product.getName());
        assertEquals(1000, product.getPrice());
        assertEquals(10, product.getStock());
        assertEquals("탄산2+1", product.getPromotion());
        assertEquals(10, product.getPromotionStock());

        Product nonPromoProduct = createProduct("물", 500, 20, null);
        assertEquals("물", nonPromoProduct.getName());
        assertEquals(500, nonPromoProduct.getPrice());
        assertEquals(20, nonPromoProduct.getStock());
        assertNull(nonPromoProduct.getPromotion());
        assertEquals(0, nonPromoProduct.getPromotionStock());
    }

    private void testReduceStock() {
        Product product = createProduct("콜라", 1000, 10, "탄산2+1");
        product.reduceStock(5);
        assertEquals(5, product.getStock());
        assertEquals(5, product.getPromotionStock());

        product.reduceStock(5);
        assertEquals(0, product.getStock());
        assertEquals(0, product.getPromotionStock());

        try {
            product.reduceStock(1);
            fail("예외가 발생해야 합니다.");
        } catch (IllegalArgumentException e) {
            assertEquals("콜라의 재고가 부족합니다.", e.getMessage());
        }
    }

    private void testDisplay() {
        Product product = createProduct("콜라", 1000, 0, "탄산2+1");
        product.display();

        Product nonPromoProduct = createProduct("물", 500, 0, null);
        nonPromoProduct.display();

        Product availableProduct = createProduct("사이다", 1000, 5, null);
        availableProduct.display();
    }

    private Product createProduct(String name, int price, int stock, String promotion) {
        return new Product(name, price, stock, promotion);
    }

    private void assertEquals(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("기대 값: " + expected + ", 실제 값: " + actual);
        }
    }

    private void assertNull(Object obj) {
        if (obj != null) {
            throw new AssertionError("객체가 null이 아님: " + obj);
        }
    }

    private void fail(String message) {
        throw new AssertionError(message);
    }
}
