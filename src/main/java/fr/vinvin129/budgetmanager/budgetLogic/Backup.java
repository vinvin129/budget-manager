package fr.vinvin129.budgetmanager.budgetLogic;

import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;

import java.io.*;
import java.util.Optional;

/**
 * Class for save and restore the main {@link BudgetMoment} object
 */
public class Backup {
    public static final Backup INSTANCE = new Backup();
    private final File file = new File("backup.txt");

    private Backup() {
    }

    /**
     * Save the main {@link BudgetMoment}
     * @param budgetMoment the {@link BudgetMoment} to save
     * @throws IOException if it can't be saved
     */
    public void save(BudgetMoment budgetMoment) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(budgetMoment);
        oos.close();
    }

    /**
     * Load the main {@link BudgetMoment} from the disk
     * @return the {@link BudgetMoment} of the main budget
     * @throws IOException if file can't be loaded
     */
    public Optional<BudgetMoment> load() throws IOException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        try {
            if (ois.readObject() instanceof BudgetMoment budgetMoment) {
                return Optional.of(budgetMoment);
            } else {
                return Optional.empty();
            }
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }
}
