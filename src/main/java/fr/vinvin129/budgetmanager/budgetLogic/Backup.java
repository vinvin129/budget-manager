package fr.vinvin129.budgetmanager.budgetLogic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Class for save and restore {@link BudgetMoment} list for each month
 */
public class Backup {
    static class Saved {
        public Period period;
        public BudgetMoment budgetMoment;

        public Saved() {

        }

        public Saved(Period period, BudgetMoment budgetMoment) {
            this.period = period;
            this.budgetMoment = budgetMoment;
        }
    }

    public static final Backup INSTANCE = new Backup();
    private final File file = new File("backup.json");

    private Backup() {
    }

    /**
     * Save {@link BudgetMoment} list for each month
     * @param history the {@link BudgetMoment} list for each month to save
     * @throws IOException if it can't be saved
     */
    public void save(Map<Period, BudgetMoment> history) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Saved> output = new ArrayList<>();
        history.forEach((period, budgetMoment) -> output.add(new Saved(period, budgetMoment)));

        objectMapper.writeValue(file, output);
    }

    /**
     * Load the history from the disk
     * @return the history or any if it can't be loaded
     */
    public Optional<Map<Period, BudgetMoment>> load() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ArrayList<Saved> input = objectMapper.readValue(file, new TypeReference<>() {
            });

            Map<Period, BudgetMoment> history = new TreeMap<>();
            input.forEach(saved -> history.put(saved.period, saved.budgetMoment));
            return Optional.of(history);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
