package com.outlay.firebase.dto.adapter;

import com.outlay.firebase.dto.ExpenseDto;
import com.outlay.domain.model.Category;
import com.outlay.domain.model.Expense;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class ExpenseAdapter {

    public Expense toExpense(ExpenseDto expenseDto) {
        Expense expense = new Expense();
        expense.setNote(expenseDto.getNote());
        expense.setAmount(new BigDecimal(expenseDto.getAmount()));
        expense.setId(expenseDto.getId());
        expense.setReportedWhen(new Date(expenseDto.getReportedWhen()));

        Category category = new Category();
        category.setId(expenseDto.getCategoryId());
        expense.setCategory(category);

        return expense;
    }

    public ExpenseDto fromExpense(Expense expense) {
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setNote(expense.getNote());
        expenseDto.setAmount(expense.getAmount().toString());
        expenseDto.setId(expense.getId());
        expenseDto.setReportedWhen(expense.getReportedWhen().getTime());
        expenseDto.setCategoryId(expense.getCategory().getId());

        return expenseDto;
    }
}
