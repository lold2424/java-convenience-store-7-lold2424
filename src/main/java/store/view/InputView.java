package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.message.IOMessage;
import store.message.InputErrorMessage;

public class InputView {
    public String readPurchaseRequest() {
        System.out.println(IOMessage.INPUT_PRODUCT_AND_QUANTITY.getMessage());
        String input = Console.readLine();
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(InputErrorMessage.EMPTY_INPUT.getMessage());
        }
        return input;
    }
}
