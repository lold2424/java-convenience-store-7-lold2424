package store.message;

public enum InputErrorMessage {
    INVALID_FORMAT("[ERROR] 올바르지 않은 형식으로 입력했습니다."),
    INVALID_QUANTITY("[ERROR] 수량이 유효한 숫자가 아닙니다."),
    EMPTY_INPUT("[ERROR] 입력이 비어있습니다."),
    INVALID_PRODUCT("[ERROR] 존재하지 않는 상품입니다."),
    EXCEEDS_STOCK("[ERROR] 요청한 수량이 재고보다 많습니다.");;

    private String message;

    private InputErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
