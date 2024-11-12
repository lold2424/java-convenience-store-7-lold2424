package store.message;

public enum ViewMessage {
    WELCOME("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n"),
    ENTER_PURCHASE_ITEMS("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"),
    MEMBERSHIP_PROMPT("멤버십 할인을 받으시겠습니까? (Y/N)"),
    PROMOTION_SUGGESTION("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"),
    NON_PROMO_WARNING("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"),
    THANK_YOU("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)"),
    RECEIPT_HEADER("\n==============W 편의점================"),
    RECEIPT_ITEM_FORMAT("%s\t\t%d\t%s"),
    RECEIPT_GIFT_HEADER("=============증\t정==============="),
    RECEIPT_GIFT_FORMAT("%s\t\t%d"),
    RECEIPT_FOOTER("===================================="),
    RECEIPT_TOTAL("총구매액\t\t%d\t%s"),
    RECEIPT_PROMO_DISCOUNT("행사할인\t\t\t-%s"),
    RECEIPT_MEMBERSHIP_DISCOUNT("멤버십할인\t\t\t-%s"),
    RECEIPT_FINAL_AMOUNT("내실돈\t\t\t %s");

    private final String message;

    ViewMessage(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
