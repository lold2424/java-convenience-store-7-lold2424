package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;
import store.util.DateProvider;

import java.time.LocalDate;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final DateProvider dateProvider;

    public Promotion(String name, int buy, int get, LocalDate startDate, LocalDate endDate, DateProvider dateProvider) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dateProvider = dateProvider;
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    public boolean isActive() {
        LocalDate currentDate = DateTimes.now().toLocalDate();
        return !currentDate.isBefore(startDate) && !currentDate.isAfter(endDate);
    }
}