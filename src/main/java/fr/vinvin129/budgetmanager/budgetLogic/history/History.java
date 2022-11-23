package fr.vinvin129.budgetmanager.budgetLogic.history;

import fr.vinvin129.budgetmanager.budgetLogic.Period;
import fr.vinvin129.budgetmanager.budgetLogic.Spent;
import fr.vinvin129.budgetmanager.budgetLogic.budgets.Budget;
import fr.vinvin129.budgetmanager.budgetLogic.budgets.BudgetController;
import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

public final class History implements HistoryNav<Budget> {
    private BudgetController controller;
    private final TreeMap<Period, BudgetMoment> history = new TreeMap<>();
    private Budget actualModel;
    private Period actualPeriod;

    public static final History INSTANCE = new History();

    private History () {
    }

    public void initialize(BudgetMoment initialMoment) throws IllegalBudgetSizeException {
        this.history.clear();
        this.controller = new BudgetController(initialMoment);
        this.actualModel = this.controller.getModel();
        this.actualPeriod = createPeriod();
        this.history.put(this.actualPeriod, this.actualModel.getMoment());
    }

    @Override
    public Budget previousMonth() {
        Map.Entry<Period, BudgetMoment> previous = this.history.lowerEntry(this.actualPeriod);
        try {
            return previous != null ? Budget.createModel(previous.getValue(), this.controller) : null;
        } catch (IllegalBudgetSizeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Budget nextMonth() {
        Map.Entry<Period, BudgetMoment> next = this.history.higherEntry(this.actualPeriod);
        try {
            return next != null ? Budget.createModel(next.getValue(), this.controller) : null;
        } catch (IllegalBudgetSizeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Budget newMonth() {
        BudgetMoment newMoment = procedureToCreateNewMoment(
                this.history.values().stream().reduce((last, next) -> next).orElseThrow());
        this.history.put(this.actualPeriod = newPeriod(), newMoment);
        try {
            return this.actualModel = Budget.createModel(newMoment, this.controller);
        } catch (IllegalBudgetSizeException e) {
            throw new RuntimeException(e);
        }
    }

    private Period newPeriod() {
        Period actualPeriod = this.history.keySet().stream().reduce((last, next) -> next).orElse(null);
        if (actualPeriod == null) {
            return createPeriod();
        }
        Calendar next = Calendar.getInstance();
        next.add(Calendar.MONTH, 1);
        return createPeriod(next);
    }

    public static Period createPeriod() {
        return createPeriod(Calendar.getInstance());
    }

    public static Period createPeriod(Calendar calendar) {
        return new Period(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
    }

    private double newBalanceBudgetMomentCalculate(BudgetMoment lastMoment) {
        double newBalance = lastMoment.balance() + lastMoment.allocationPerMonth();
        double allocationSum = Arrays.stream(lastMoment.categoryMoments())
                .filter(categoryMoment -> categoryMoment.budgetMoment() != null)
                .mapToDouble(CategoryMoment::allocationPerMonth)
                .sum();
        newBalance -= allocationSum;
        return newBalance;
    }

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
        return this.actualModel;
    }

    @Override
    public Period getActualPeriod() {
        return this.actualPeriod;
    }
}
