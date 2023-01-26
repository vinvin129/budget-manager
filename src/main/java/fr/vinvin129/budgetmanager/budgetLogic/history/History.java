package fr.vinvin129.budgetmanager.budgetLogic.history;

import fr.vinvin129.budgetmanager.budgetLogic.Backup;
import fr.vinvin129.budgetmanager.budgetLogic.Period;
import fr.vinvin129.budgetmanager.budgetLogic.Spent;
import fr.vinvin129.budgetmanager.budgetLogic.budgets.Budget;
import fr.vinvin129.budgetmanager.budgetLogic.budgets.BudgetController;
import fr.vinvin129.budgetmanager.budgetLogic.categories.BudgetCategory;
import fr.vinvin129.budgetmanager.budgetLogic.categories.Category;
import fr.vinvin129.budgetmanager.budgetLogic.categories.StandardCategory;
import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.events.EventT;
import fr.vinvin129.budgetmanager.events.Observable;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;

import java.io.IOException;
import java.util.*;

/**
 * this singleton class control the history of a main {@link BudgetController} in the time
 * @author vinvin129
 */
public final class History extends Observable implements HistoryNav<Budget> {
    /**
     * the main {@link BudgetController}
     */
    private BudgetController mainController;
    /**
     * the Map of {@link BudgetMoment} by {@link Period}
     */
    private final TreeMap<Period, BudgetMoment> history = new TreeMap<>();
    /**
     * the actual {@link Period}
     */
    private Period actualPeriod;

    /**
     * the unique instance of {@link History}
     */
    public static final History INSTANCE = new History();

    private History () {
    }

    /**
     * initialize or reinitialize history from a {@link BudgetController} and this {@link Budget}
     * @param mainController the {@link BudgetController} object
     */
    public void initialize(BudgetController mainController) {
        this.history.clear();
        this.mainController = mainController;
        this.actualPeriod = createPeriod();
        this.history.put(this.actualPeriod, this.mainController.getModel().getMoment());
    }

    /**
     * Initialise ou réinitialise l'historique depuis une map des {@link BudgetMoment} par les {@link Period}.
     *  Si des mois sont manquants entre le mois actuel et le dernier enregistré, ils seront calculés.
     * @param history Une map des {@link BudgetMoment} par les {@link Period}
     * @return Le controller principal {@link BudgetController}, configuré sur le mois en cours, ou rien si la map n'était pas correcte
     * @throws IllegalBudgetSizeException si le budget principal ou ses sous budgets, sont incohérents
     */
    public Optional<BudgetController> initialize(TreeMap<Period, BudgetMoment> history) throws IllegalBudgetSizeException {
        this.history.clear();
        this.actualPeriod = null;
        this.mainController = null;

        Period actualPeriod = History.createPeriod();
        Period lastHistoryPeriod = history.keySet().stream().reduce((last, next) -> next).orElse(null);
        if (lastHistoryPeriod == null) {
            return Optional.empty();
        }

        /* Ajout des mois manquants entre le dernier de l'historique et le mois en cours, s'il y en a */
        while (actualPeriod.compareTo(lastHistoryPeriod) > 0) {
            BudgetMoment lastHistoryMoment = history.get(lastHistoryPeriod);
            Period nextPeriod = nextPeriod(lastHistoryPeriod);
            history.put(nextPeriod, procedureToCreateNewMoment(lastHistoryMoment));
            lastHistoryPeriod = nextPeriod;
        }

        /* Vérification de l'absence de 'trou' entre les périodes */
        Set<Period> normalPeriodSet = new HashSet<>();
        Set<Period> actualPeriodSet = history.keySet();
        Period p = actualPeriodSet.stream().reduce((last, next) -> last).orElseThrow();
        normalPeriodSet.add(p);
        for (int i = 1; i < actualPeriodSet.size(); i++) {
            Period next = nextPeriod(p);
            normalPeriodSet.add(next);
            p = next;
        }
        if (!actualPeriodSet.equals(normalPeriodSet)) {
            return Optional.empty();
        }

        /* Création du controleur et initialisation de l'historique */
        BudgetController controller = new BudgetController(history.get(actualPeriod));
        this.mainController = controller;
        this.actualPeriod = actualPeriod;
        this.history.putAll(history);
        return Optional.of(controller);
    }

    /**
     * Permet de sauvegarder tout l'historique dans un fichier
     * @see Backup#save(Map)
     * @throws IOException
     */
    public void save() throws IOException {
        recalculeHistoriqueAPartirMoisActuel();
        this.history.put(this.actualPeriod, this.mainController.getModel().getMoment());
        Backup.INSTANCE.save(history);
    }

    @Override
    public Budget previousMonth() {
        Period previous = this.history.lowerKey(this.actualPeriod);
        return setModelFromEntry(previous);
    }

    @Override
    public Budget nextMonth() {
        Period next = this.history.higherKey(this.actualPeriod);
        return setModelFromEntry(next);
    }

    @Override
    public boolean hasPrevious() {
        return this.history.lowerKey(this.actualPeriod) != null;
    }

    @Override
    public boolean hasNext() {
        return this.history.higherKey(this.actualPeriod) != null;
    }

    /**
     * set actual model and {@link Period} from a {@link Period} and this linked {@link BudgetMoment}
     * call {@link History#recalculeHistoriqueAPartirMoisActuel()}
     * @param period the period to set
     * @return a {@link Budget} generated from the entry {@link BudgetMoment}
     */
    private Budget setModelFromEntry(Period period) {
        //Save actual model if exists and recalculate the history
        recalculeHistoriqueAPartirMoisActuel();
        this.history.put(this.actualPeriod, this.mainController.getModel().getMoment());

        //get new model from history
        try {
            Budget model = null;
            if (period != null) {
                BudgetMoment newMoment = this.history.get(period);
                model = Budget.createModel(newMoment, this.mainController);
                this.mainController.setModel(model);
                this.actualPeriod = period;
            }
            this.fire(EventT.HISTORY_MONTH_CHANGE);
            return model;
        } catch (IllegalBudgetSizeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Budget newMonth() {
        BudgetMoment newMoment;
        if (this.actualPeriod.equals(this.history.keySet().stream().reduce((last, next) -> next).orElseThrow())) {
            newMoment = procedureToCreateNewMoment(this.mainController.getModel().getMoment());
        } else {
            newMoment = procedureToCreateNewMoment(
                    this.history.values().stream().reduce((last, next) -> next).orElseThrow());
        }
        this.history.put(newPeriod(), newMoment);
        try {
            this.fire(EventT.HISTORY_MONTH_CHANGE);
            return Budget.createModel(newMoment, this.mainController);
        } catch (IllegalBudgetSizeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generate a new {@link Period} according to actual periods from history map
     * @see History#createPeriod(Calendar) used if history map isn't empty
     * @see History#createPeriod() used if history map is empty
     * @return a new {@link Period} object
     */
    private Period newPeriod() {
        Period latestPeriod = this.history.keySet().stream().reduce((last, next) -> next).orElse(null);
        if (latestPeriod == null) {
            return createPeriod();
        }
        return nextPeriod(latestPeriod);
    }

    /**
     * create a {@link Period} from actual {@link Calendar}
     * @return a {@link Period} object
     */
    public static Period createPeriod() {
        return createPeriod(Calendar.getInstance());
    }

    /**
     * create a {@link Period} from a specific {@link Calendar}
     * @param calendar the {@link Calendar} object
     * @return a {@link Period} object
     */
    public static Period createPeriod(Calendar calendar) {
        return new Period(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
    }

    /**
     * calculate the balance of budget for the next month
     * @param lastMoment the {@link BudgetMoment} object
     * @return the new balance
     */
    private double newBalanceBudgetMomentCalculate(BudgetMoment lastMoment) {
        double newBalance = lastMoment.balance() + lastMoment.allocationPerMonth();
        double allocationSum = Arrays.stream(lastMoment.categoryMoments())
                .filter(categoryMoment -> categoryMoment.budgetMoment() != null)
                .mapToDouble(CategoryMoment::allocationPerMonth)
                .sum();
        newBalance -= allocationSum;
        return newBalance;
    }

    /**
     * the procedure to generate the {@link BudgetMoment} for the next month
     * @param latestMoment origin {@link BudgetMoment}
     * @return the m+1 {@link BudgetMoment}
     */
    private BudgetMoment procedureToCreateNewMoment(BudgetMoment latestMoment) {
        double newBalance = newBalanceBudgetMomentCalculate(latestMoment);
        CategoryMoment[] newCategoryMoments = Arrays.stream(latestMoment.categoryMoments())
                .map(cm -> new CategoryMoment(
                        cm.name(),
                        cm.allocationPerMonth(),
                        cm.budgetMoment() != null ? newBalanceBudgetMomentCalculate(cm.budgetMoment()) : cm.allocationPerMonth(),
                        new Spent[]{},
                        cm.budgetMoment() != null ? procedureToCreateNewMoment(cm.budgetMoment()) : null
                ))
                .toArray(CategoryMoment[]::new);

        return new BudgetMoment(
                latestMoment.name(),
                latestMoment.allocationPerMonth(),
                newBalance,
                newCategoryMoments
        );
    }

    @Override
    public Budget getActualModel() {
        return this.mainController.getModel();
    }

    @Override
    public Period getActualPeriod() {
        return this.actualPeriod;
    }

    /**
     * Recalcule l'historique des périodes supérieures à la période actuelle à partir du {@link Budget} actuel.
     * Seuls les soldes, en fonctions des dépenses ajoutées dans le passé, sont mises à jour.
     * Les catégories doivent être au même nombre.
     */
    public void recalculeHistoriqueAPartirMoisActuel() {
        Period nextPeriod = this.history.higherKey(this.actualPeriod);
        if (nextPeriod == null) {
            return;
        }
        Budget actualModel = this.mainController.getModel();
        BudgetMoment actualMoment = actualModel.getMoment();
        if (this.history.get(this.actualPeriod).equals(actualMoment)) {
            return;
        }

        this.history.put(this.actualPeriod, actualMoment);
        this.history.tailMap(this.actualPeriod, false)
                .forEach((period, budgetMoment) -> {
                    try {
                        recalculeMoisProchain(period);
                    } catch (IllegalBudgetSizeException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * Recalcule l'historique d'une période spécifique à partir de la période avant elle
     * Seuls les soldes, en fonctions des dépenses ajoutées dans le passé, sont mises à jour.
     * Les catégories doivent être au même nombre.
     * @param moisProchain la période qui doit être recalculée
     * @throws IllegalBudgetSizeException si il y a un problème de conception du budget
     */
    private void recalculeMoisProchain(Period moisProchain) throws IllegalBudgetSizeException {
        Map.Entry<Period, BudgetMoment> previousEntry = this.history.lowerEntry(moisProchain);
        if (previousEntry == null) {
            return;
        }
        BudgetMoment previousMonth = previousEntry.getValue();
        BudgetMoment nextMonthVirgin = procedureToCreateNewMoment(previousMonth);
        Budget nextMonthModel = Budget.createModel(nextMonthVirgin, this.mainController);
        ajoutDepensesMoisRecalcule(this.history.get(moisProchain), nextMonthModel);
        this.history.put(moisProchain, nextMonthModel.getMoment());
    }

    /**
     * Ajoute les dépenses {@link Spent} qui n'existent pas déjà dans un {@link Budget}, à partir d'un {@link BudgetMoment} référentiel
     * @param base le {@link BudgetMoment} référentiel
     * @param nextMonthModel le {@link Budget} où les dépenses doivent être ajoutées
     */
    private void ajoutDepensesMoisRecalcule(BudgetMoment base, Budget nextMonthModel) {
        if (base.categoryMoments().length != nextMonthModel.getCategoryControllers().length) {
            return;
        }
        for (int i = 0; i < base.categoryMoments().length; i++) {
            CategoryMoment categoryMoment = base.categoryMoments()[i];
            Category categoryNextMonthModel = nextMonthModel.getCategoryControllers()[i].getModel();
            if (categoryMoment.budgetMoment() != null && categoryNextMonthModel instanceof BudgetCategory budgetCategory) {
                ajoutDepensesMoisRecalcule(categoryMoment.budgetMoment(), budgetCategory.getBudgetController().getModel());
            } else if (categoryMoment.budgetMoment() == null && categoryNextMonthModel instanceof StandardCategory) {
                Arrays.stream(categoryMoment.expenses())
                        .filter(spent -> !Arrays.asList(categoryNextMonthModel.getSpentList()).contains(spent))
                        .forEach(categoryNextMonthModel.getController()::addSpent);
            }
        }
    }

    /**
     * Crée la période suivante par rapport à celle en paramètre
     * @param period la période de référence
     * @return la période suivante
     */
    private static Period nextPeriod(Period period) {
        Calendar next = Calendar.getInstance();
        next.set(Calendar.DATE, 1);
        next.set(Calendar.MONTH, period.month());
        next.set(Calendar.YEAR, period.year());
        next.add(Calendar.MONTH, 1);

        return createPeriod(next);
    }
}
