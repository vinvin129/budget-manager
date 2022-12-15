package fr.vinvin129.budgetmanager.budgetLogic.history;

import fr.vinvin129.budgetmanager.budgetLogic.Period;
import fr.vinvin129.budgetmanager.budgetLogic.Spent;
import fr.vinvin129.budgetmanager.budgetLogic.budgets.Budget;
import fr.vinvin129.budgetmanager.budgetLogic.budgets.BudgetController;
import fr.vinvin129.budgetmanager.budgetLogic.categories.BudgetCategory;
import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.events.EventT;
import fr.vinvin129.budgetmanager.events.Observable;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

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

    @Override
    public Budget previousMonth() {
        Map.Entry<Period, BudgetMoment> previous = this.history.lowerEntry(this.actualPeriod);
        return setModelFromEntry(previous);
    }

    @Override
    public Budget nextMonth() {
        Map.Entry<Period, BudgetMoment> next = this.history.higherEntry(this.actualPeriod);
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
     * set actual model and {@link Period} from an entry of a {@link Period} and {@link BudgetMoment}
     * @param entry the entry to set
     * @return a {@link Budget} generated from the entry {@link BudgetMoment}
     */
    private Budget setModelFromEntry(Map.Entry<Period, BudgetMoment> entry) {
        //Save actual model if exists
        Budget actualBudget = this.mainController.getModel();
        if (this.actualPeriod != null && actualBudget != null) {
            this.history.put(this.actualPeriod, actualBudget.getMoment());
        }

        //get new model from history
        try {
            Budget model = entry != null ? Budget.createModel(entry.getValue(), this.mainController) : null;
            if (model != null) {
                this.mainController.setModel(model);
                this.actualPeriod = entry.getKey();
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
        Period actualPeriod = this.history.keySet().stream().reduce((last, next) -> next).orElse(null);
        if (actualPeriod == null) {
            return createPeriod();
        }
        Calendar next = Calendar.getInstance();
        next.set(Calendar.MONTH, actualPeriod.month());
        next.set(Calendar.YEAR, actualPeriod.year());
        next.add(Calendar.MONTH, 1);
        return createPeriod(next);
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
     * Updates moments in history in according to changes in actual budget model to preserve logic
     */
    public void updateFutureFromPresent() throws IllegalBudgetSizeException {
        Period period = this.actualPeriod;
        BudgetMoment origin = this.history.get(period);
        do {
            updateFutureFromPresent(origin);
            Map.Entry<Period, BudgetMoment> newEntry = this.history.higherEntry(period);
            if (newEntry != null) {
                period = newEntry.getKey();
                origin = newEntry.getValue();
            } else {
                period = null;
                origin = null;
            }
        } while (period != null && origin != null);
    }

    public void updateFutureFromPresent(BudgetMoment origin) throws IllegalBudgetSizeException {
        BudgetMoment actual = this.mainController.getModel().getMoment();
        BudgetMoment previousFuture = this.history.values().stream().reduce((last, next) -> next).orElse(null);
        if (!origin.equals(actual) && previousFuture != null) {
            BudgetController futureController = new BudgetController(procedureToCreateNewMoment(actual));
            Budget futureModel = futureController.getModel();
            updateFutureFromPreviousFuture(previousFuture, futureModel);
            Period nextPeriod = this.history.keySet().stream().reduce((last, next) -> next).orElseThrow();
            this.history.put(nextPeriod, futureModel.getMoment());
        }
    }

    private static void updateFutureFromPreviousFuture(BudgetMoment previousFuture, Budget futureModel) {
        if (previousFuture.categoryMoments().length != futureModel.getCategoryControllers().length) {
            return;
        }
        for (int i = 0; i < previousFuture.categoryMoments().length; i++) {
            CategoryMoment categoryMoment = previousFuture.categoryMoments()[i];
            if (categoryMoment.budgetMoment() != null) {
                updateFutureFromPreviousFuture(
                        categoryMoment.budgetMoment(),
                        ((BudgetCategory) futureModel.getCategoryControllers()[i].getModel())
                                .getBudgetController().getModel());
            } else {
                Arrays.stream(categoryMoment.expenses())
                        .forEach(futureModel.getCategoryControllers()[i]::addSpent);
            }
        }
    }
}
