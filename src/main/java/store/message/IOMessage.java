package store.message;

public enum IOMessage {
    START_MESSAGE("안녕하세요. W편의점입니다.\n" + "현재 보유하고 있는 상품입니다.");

    private String message;

    IOMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
