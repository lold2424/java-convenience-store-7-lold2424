package store.util;

import java.time.LocalDate;

public class FixedDateProvider implements DateProvider {
    private final LocalDate fixedDate;

    public FixedDateProvider(LocalDate fixedDate) {
        this.fixedDate = fixedDate;
    }

    @Override
    public LocalDate getCurrentDate() {
        return fixedDate;
    }
}