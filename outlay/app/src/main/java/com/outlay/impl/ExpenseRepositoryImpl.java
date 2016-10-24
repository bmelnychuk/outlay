package com.outlay.impl;

import android.content.Context;

import com.outlay.api.OutlayDatabaseApi;
import com.outlay.domain.model.Category;
import com.outlay.domain.model.Expense;
import com.outlay.domain.model.Report;
import com.outlay.domain.repository.ExpenseRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class ExpenseRepositoryImpl implements ExpenseRepository {
    private OutlayDatabaseApi outlayDatabaseApi;
    private Context context;

    @Inject
    public ExpenseRepositoryImpl(
            OutlayDatabaseApi outlayDatabaseApi,
            Context context
    ) {
        this.outlayDatabaseApi = outlayDatabaseApi;
        this.context = context;
    }

    @Override
    public Observable<Expense> saveExpense(Expense expense) {
        com.outlay.dao.Expense expense1 = new com.outlay.dao.Expense();
        expense1.setId(expense.getId());
        expense1.setAmount(expense.getAmount().doubleValue());
        expense1.setCategoryId(expense.getCategory().getId());
        expense1.setNote(expense.getNote());
        expense1.setReportedAt(expense.getReportedAt());


        return Observable.create(subscriber -> {
            com.outlay.dao.Expense dbExpense = outlayDatabaseApi.insertExpense(expense1);

            Expense result = new Expense();
            result.setId(dbExpense.getId());
            result.setAmount(new BigDecimal(dbExpense.getAmount().toString()));
            result.setNote(dbExpense.getNote());
            result.setReportedAt(dbExpense.getReportedAt());

            Category category = new Category();
            category.setId(dbExpense.getCategoryId());
            result.setCategory(category);
        });
    }

    @Override
    public Observable<List<Expense>> getExpenses(Date startDate, Date endDate) {
        return outlayDatabaseApi.getExpenses(startDate, endDate).map(databaseExpenses -> {
            List<Expense> result = new ArrayList<>();
            for (com.outlay.dao.Expense dbExpense : databaseExpenses) {
                Expense exp = new Expense();
                exp.setId(dbExpense.getId());
                exp.setAmount(new BigDecimal(dbExpense.getAmount().toString()));
                exp.setNote(dbExpense.getNote());
                exp.setReportedAt(dbExpense.getReportedAt());

                Category category = new Category();
                category.setId(dbExpense.getCategoryId());
                category.setTitle(dbExpense.getCategory().getTitle());
                category.setColor(dbExpense.getCategory().getColor());
                category.setOrder(dbExpense.getCategory().getOrder());
                category.setIcon(dbExpense.getCategory().getIcon());
                exp.setCategory(category);
                result.add(exp);
            }

            return result;
        });
    }
}
