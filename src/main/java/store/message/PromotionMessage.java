package store.message;

public enum PromotionMessage {
    EXTRA_FREE_ITEMS_OFFER("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"),
    NON_PROMOTIONAL_ITEMS("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"),
    NON_PROMOTIONAL_ITEMS_PURCHASED("프로모션 적용 없이 구매를 진행합니다."),
    NON_PROMOTIONAL_ITEMS_CANCELED("해당 상품 구매를 취소했습니다."),
    INVALID_INPUT("[ERROR] 잘못된 입력입니다. Y 또는 N을 입력해 주세요.");

    private final String message;

    PromotionMessage(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }

    @Override
    public String toString() {
        return message;
    }
}
