package com.outlay.firebase.dto.adapter;

import com.outlay.domain.model.Expense;
import com.outlay.firebase.dto.ExpenseDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class ExpenseAdapter {
    private CategoryAdapter categoryAdapter;

    public ExpenseAdapter() {
        this.categoryAdapter = new CategoryAdapter();
    }

    public Expense toExpense(ExpenseDto expenseDto) {
        Expense expense = new Expense();
        expense.setNote(expenseDto.getNote());
        expense.setAmount(new BigDecimal(expenseDto.getAmount()));
        expense.setId(expenseDto.getId());
        expense.setReportedAt(new Date(expenseDto.getReportedAt()));

        expense.setCategory(categoryAdapter.toCategory(expenseDto.getCategory()));

        return expense;
    }

    public ExpenseDto fromExpense(Expense expense) {
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setNote(expense.getNote());
        expenseDto.setAmount(expense.getAmount().toString());
        expenseDto.setId(expense.getId());
        expenseDto.setReportedAt(expense.getReportedAt().getTime());
        expenseDto.setCategory(categoryAdapter.fromCategory(expense.getCategory()));

        return expenseDto;
    }

    public List<Expense> toExpenses(List<ExpenseDto> expenseDtos) {
        List<Expense> result = new ArrayList<>(expenseDtos.size());
        for (ExpenseDto c : expenseDtos) {
            result.add(toExpense(c));
        }
        return result;
    }

    public List<ExpenseDto> fromExpenses(List<Expense> expenses) {
        List<ExpenseDto> result = new ArrayList<>(expenses.size());
        for (Expense c : expenses) {
            result.add(fromExpense(c));
        }
        return result;
    }
}
