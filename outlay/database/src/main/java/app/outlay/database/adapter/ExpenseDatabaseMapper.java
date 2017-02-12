package app.outlay.database.adapter;

import app.outlay.domain.model.Category;
import app.outlay.domain.model.Expense;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bmelnychuk on 10/25/16.
 */

public class ExpenseDatabaseMapper {
    private CategoryDatabaseMapper categoryDatabaseMapper;


    public ExpenseDatabaseMapper(CategoryDatabaseMapper categoryDatabaseMapper) {
        this.categoryDatabaseMapper = categoryDatabaseMapper;
    }

    public Expense toExpense(app.outlay.database.dao.Expense dbExpense) {
        Expense result = new Expense();
        result.setId(dbExpense.getId().toString());
        result.setAmount(new BigDecimal(dbExpense.getAmount().toString()));
        result.setNote(dbExpense.getNote());
        result.setReportedWhen(dbExpense.getReportedAt());

        Category category = categoryDatabaseMapper.toCategory(dbExpense.getCategory());
        result.setCategory(category);

        return result;
    }

    public app.outlay.database.dao.Expense fromExpense(Expense expense) {
        app.outlay.database.dao.Expense dbExpense = new app.outlay.database.dao.Expense();
        dbExpense.setId(expense.getId() == null ? null : Long.valueOf(expense.getId()));
        dbExpense.setAmount(expense.getAmount().doubleValue());
        dbExpense.setCategoryId(
                expense.getCategory().getId() == null ? null : Long.valueOf(expense.getCategory().getId())
        );
        dbExpense.setNote(expense.getNote());
        dbExpense.setReportedAt(expense.getReportedWhen());

        return dbExpense;
    }

    public List<Expense> toExpenses(List<app.outlay.database.dao.Expense> daoExpenses) {
        List<Expense> result = new ArrayList<>(daoExpenses.size());
        for (app.outlay.database.dao.Expense c : daoExpenses) {
            result.add(toExpense(c));
        }
        return result;
    }

    public List<app.outlay.database.dao.Expense> fromExpenses(List<Expense> expenses) {
        List<app.outlay.database.dao.Expense> result = new ArrayList<>(expenses.size());
        for (Expense c : expenses) {
            result.add(fromExpense(c));
        }
        return result;
    }
}
