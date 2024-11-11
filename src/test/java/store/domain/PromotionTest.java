package store.domain;

import store.util.DateProvider;
import store.util.FixedDateProvider;

import java.time.LocalDate;

public class PromotionTest {
    public static void main(String[] args) {
        PromotionTest tester = new PromotionTest();
        tester.testIsActive();
        System.out.println("모든 Promotion 테스트를 통과했습니다.");
    }

    private void testIsActive() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        testPromotionActiveStatus(startDate, endDate, LocalDate.of(2024, 6, 15), true);
        testPromotionActiveStatus(startDate, endDate, LocalDate.of(2023, 12, 31), false);
        testPromotionActiveStatus(startDate, endDate, LocalDate.of(2025, 1, 1), false);
        testPromotionActiveStatus(startDate, endDate, startDate, true);
        testPromotionActiveStatus(startDate, endDate, endDate, true);
    }

    private void testPromotionActiveStatus(LocalDate startDate, LocalDate endDate, LocalDate currentDate, boolean expectedStatus) {
        DateProvider dateProvider = new FixedDateProvider(currentDate);
        Promotion promotion = new Promotion("탄산2+1", 2, 1, startDate, endDate, dateProvider);
        assertEquals(expectedStatus, promotion.isActive());
    }

    private void assertEquals(boolean expected, boolean actual) {
        if (expected != actual) {
            throw new AssertionError("기대 값: " + expected + ", 실제 값: " + actual);
        }
    }
}
