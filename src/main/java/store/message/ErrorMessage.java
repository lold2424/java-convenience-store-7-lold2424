package store.message;

public enum ErrorMessage {
    FILE_NOT_FOUND("[ERROR] 파일을 찾을 수 없습니다.: "),
    FILE_READ_ERROR("[ERROR] 파일을 읽을 수 없습니다.: "),
    INVALID_FILE_HEADER("[ERROR] 파일 헤더가 예상과 다릅니다. 파일명: "),
    PRODUCT_NOT_FOUND("[ERROR] 해당 상품을 찾을 수 없습니다: "),
    INVALID_INPUT_FORMAT("[ERROR] 올바르지 않은 형식으로 입력했습니다: "),
    QUANTITY_NOT_NUMBER("[ERROR] 수량은 숫자여야 합니다: "),
    QUANTITY_LESS_THAN_ONE("[ERROR] 수량은 1 이상이어야 합니다: "),
    STOCK_EXCEEDED("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    PURCHASE_CANCELLED("[ERROR] 구매가 취소되었습니다."),
    INVALID_YN_INPUT("[ERROR] 잘못된 입력입니다. Y 또는 N을 입력하세요."),
    INVALID_INPUT("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요."),
    INSUFFICIENT_STOCK("[ERROR] %s의 재고가 부족합니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
