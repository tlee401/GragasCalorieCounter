package GragasApp.controller;
import GragasApp.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface LogController {
    static List<Loggable> getEntries() {
        return new ArrayList<Loggable>();
    }

    void removeFood(FoodEntry entry, LocalDate date);
}
