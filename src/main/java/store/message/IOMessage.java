package store.message;

public enum IOMessage {
    START_MESSAGE("안녕하세요. W편의점입니다.\n" + "현재 보유하고 있는 상품입니다.\n"),
    INPUT_PRODUCT_AND_QUANTITY("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");

    private String message;

    IOMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
