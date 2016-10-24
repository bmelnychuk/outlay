package com.outlay.domain.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class DateSummary {
    private Date date;
    private BigDecimal dayAmount;
    private BigDecimal weekAmount;
    private BigDecimal monthAmount;
    private List<Category> categories;

    public Date getDate() {
        return date;
    }

    public DateSummary setDate(Date date) {
        this.date = date;
        return this;
    }

    public BigDecimal getDayAmount() {
        return dayAmount;
    }

    public DateSummary setDayAmount(BigDecimal dayAmount) {
        this.dayAmount = dayAmount;
        return this;
    }

    public BigDecimal getWeekAmount() {
        return weekAmount;
    }

    public DateSummary setWeekAmount(BigDecimal weekAmount) {
        this.weekAmount = weekAmount;
        return this;
    }

    public BigDecimal getMonthAmount() {
        return monthAmount;
    }

    public DateSummary setMonthAmount(BigDecimal monthAmount) {
        this.monthAmount = monthAmount;
        return this;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public DateSummary setCategories(List<Category> categories) {
        this.categories = categories;
        return this;
    }
}
