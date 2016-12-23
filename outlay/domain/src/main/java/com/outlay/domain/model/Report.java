package com.outlay.domain.model;

import com.outlay.core.utils.DateUtils;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class Report {
    private List<Expense> expenses;
    private Date startDate;
    private Date endDate;
    private Category category;

    public Category getCategory() {
        return category;
    }

    public Report setCategory(Category category) {
        this.category = category;
        return this;
    }

    public List<Expense> getExpenses() {
        if (expenses == null) {
            expenses = new ArrayList<>();
        }
        return expenses;
    }

    public Report setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Report setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Report setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal result = BigDecimal.ZERO;

        for (Expense expense : expenses) {
            result = result.add(expense.getAmount());
        }

        return result;
    }

    public BigDecimal getTotalAmount(Date start, Date end) {
        BigDecimal result = BigDecimal.ZERO;

        DateTime startDate = new DateTime(start);
        DateTime endDate = new DateTime(end);

        for (Expense expense : expenses) {
            DateTime expenseDate = new DateTime(expense.getReportedAt());
            if (expenseDate.isAfter(startDate) && expenseDate.isBefore(endDate)) {
                result = result.add(expense.getAmount());
            }
        }

        return result;
    }

    public Map<Category, Report> groupByCategory() {
        Map<Category, Report> result = new HashMap<>();

        for (Expense expense : expenses) {
            Category expenseCategory = expense.getCategory();
            Report reportForCategory = result.get(expenseCategory);
            if (reportForCategory == null) {
                reportForCategory = new Report();
                reportForCategory.setCategory(expenseCategory);
                reportForCategory.setStartDate(expense.getReportedAt());
                reportForCategory.setEndDate(expense.getReportedAt());
            } else {
                reportForCategory.setStartDate(
                        DateUtils.getMin(expense.getReportedAt(), reportForCategory.startDate)
                );
                reportForCategory.setEndDate(
                        DateUtils.getMax(expense.getReportedAt(), reportForCategory.endDate)
                );
            }
            reportForCategory.getExpenses().add(expense);

            result.put(expenseCategory, reportForCategory);
        }

        return result;
    }


    public DateSummary getDateSummary(Date date) {
        Date startOfMonth = DateUtils.getMonthStart(date);
        Date endOfMonth = DateUtils.getMonthEnd(date);

        Date startOfWeek = DateUtils.getWeekStart(date);
        Date endOfWeek = DateUtils.getWeekEnd(date);

        Date startOfDay = DateUtils.getDayStart(date);
        Date endOfDay = DateUtils.getDayEnd(date);

        BigDecimal monthAmount = getTotalAmount(startOfMonth, endOfMonth);
        BigDecimal weeklyAmount = getTotalAmount(startOfWeek, endOfWeek);
        BigDecimal dailyAmount = getTotalAmount(startOfDay, endOfDay);

        DateSummary result = new DateSummary();
        result.setDate(date);
        result.setDayAmount(dailyAmount);
        result.setWeekAmount(weeklyAmount);
        result.setMonthAmount(monthAmount);
        result.setCategories(getMostPayedCategories());
        return result;
    }

    public List<Category> getMostPayedCategories() {
        Map<Category, BigDecimal> pricesPerCategory = new HashMap<>();

        for (Expense expense : expenses) {
            BigDecimal priceForCategory = pricesPerCategory.get(expense.getCategory());
            if (priceForCategory == null) {
                priceForCategory = BigDecimal.ZERO;
            }
            priceForCategory = priceForCategory.add(expense.getAmount());
            pricesPerCategory.put(expense.getCategory(), priceForCategory);
        }

        TreeMap<Category, BigDecimal> treeMap = new TreeMap<>(new MapValueComparator(pricesPerCategory));
        treeMap.putAll(pricesPerCategory);
        List<Category> result = new ArrayList<>(treeMap.keySet());

        return result;
    }

    private static final class MapValueComparator<K, V extends Comparable> implements Comparator<K> {
        private Map<K, V> source;

        public MapValueComparator(Map<K, V> source) {
            this.source = source;
        }

        @Override
        public int compare(K o1, K o2) {
            V value1 = source.get(o1);
            V value2 = source.get(o2);
            return value2.compareTo(value1);
        }
    }
}