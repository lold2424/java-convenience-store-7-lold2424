package store.util;

import camp.nextstep.edu.missionutils.DateTimes;

import java.time.LocalDate;

public class SystemDateProvider implements DateProvider {
    @Override
    public LocalDate getCurrentDate() {
        return DateTimes.now().toLocalDate();
    }
}