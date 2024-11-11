package store.service;

import store.domain.Promotion;
import store.util.DateProvider;
import store.util.FixedDateProvider;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Map;

public class PromotionServiceTest {
    public static void main(String[] args) {
        PromotionServiceTest tester = new PromotionServiceTest();
        tester.testInitializePromotions();
        System.out.println("모든 PromotionService 테스트를 통과했습니다.");
    }

    private void testInitializePromotions() {
        // 가상의 promotions.md 내용을 가진 InputStream 생성
        String mockData = "name,buy,get,start_date,end_date\n" +
                "탄산2+1,2,1,2024-01-01,2024-12-31\n";
        InputStream mockInputStream = new ByteArrayInputStream(mockData.getBytes());

        DateProvider dateProvider = new FixedDateProvider(LocalDate.of(2024, 6, 15));
        PromotionService promotionService = new PromotionService(dateProvider);

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mockInputStream));
            String expectedHeader = "name,buy,get,start_date,end_date";
            promotionService.validateHeader(reader.readLine(), expectedHeader, "promotions.md");
            Map<String, Promotion> promotions = promotionService.parsePromotionData(promotionService.readLines(reader));

            assert promotions.containsKey("탄산2+1");
            Promotion promo = promotions.get("탄산2+1");
            assert promo.getBuy() == 2;
            assert promo.getGet() == 1;
            assert promo.isActive();

        } catch (Exception e) {
            e.printStackTrace();
            assert false : "예외가 발생했습니다.";
        }
    }
}
