package com.outlay.model;

import com.outlay.dao.Category;
import com.outlay.dao.Expense;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bogdan Melnychuk on 1/27/16.
 */
public class Report {
    private List<Expense> expenses;
    private double amount;

    public Report() {
        expenses = new ArrayList<>();
    }

    public void addExpense(Expense e) {
        expenses.add(e);
        amount += e.getAmount();
    }

    public double getAmount() {
        return amount;
    }

    public String getIcon() {
        String icon = null;
        if (expenses != null && !expenses.isEmpty()) {
            icon = expenses.get(0).getCategory().getIcon();
        }
        return icon;
    }

    public int getColor() {
        int color = 0;
        if (expenses != null && !expenses.isEmpty()) {
            color = expenses.get(0).getCategory().getColor();
        }
        return color;
    }

    public String getTitle() {
        String title = "";
        if (expenses != null && !expenses.isEmpty()) {
            title = expenses.get(0).getCategory().getTitle();
        }
        return title;
    }

    public Category getCategory() {
        Category category = null;
        if (expenses != null && !expenses.isEmpty()) {
            category = expenses.get(0).getCategory();
        }
        return category;
    }
}
