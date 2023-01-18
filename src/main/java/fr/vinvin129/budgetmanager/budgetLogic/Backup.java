package fr.vinvin129.budgetmanager.budgetLogic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Class for save and restore {@link BudgetMoment} list for each month
 */
public class Backup {
    static class Saved {
        public Period period;
        public BudgetMoment budgetMoment;
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
        ArrayList<Map<String, Pair<Period, BudgetMoment>>> output = new ArrayList<>();
        history.forEach((period, budgetMoment) -> {
            Map<String, Pair<Period, BudgetMoment>> pairs = new HashMap<>();
            pairs.put("testeee", new Pair<>(period, budgetMoment));
            output.add(pairs);
        });

        objectMapper.writeValue(file, output);
    }

    /**
     * Load the history from the disk
     * @return the history or any if it can't be loaded
     */
    public Optional<Map<Period, BudgetMoment>> load() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ArrayList<Map<String, Pair<Period, BudgetMoment>>> input = objectMapper.readValue(file, new TypeReference<>() {
            });

            Map<Period, BudgetMoment> history = new TreeMap<>();
            input.forEach(stringObjectMap -> {
                Pair<Period, BudgetMoment> pair = stringObjectMap.get("testeee");
                history.put(pair.getKey(), pair.getValue());
                    }
            );
            return Optional.of(history);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
