package com.outlay.domain.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class DaySummary {
    private Date date;
    private BigDecimal dayAmount;
    private BigDecimal weekAmount;
    private BigDecimal monthAmount;
    private List<Category> categories;

    public Date getDate() {
        return date;
    }

    public DaySummary setDate(Date date) {
        this.date = date;
        return this;
    }

    public BigDecimal getDayAmount() {
        return dayAmount;
    }

    public DaySummary setDayAmount(BigDecimal dayAmount) {
        this.dayAmount = dayAmount;
        return this;
    }

    public BigDecimal getWeekAmount() {
        return weekAmount;
    }

    public DaySummary setWeekAmount(BigDecimal weekAmount) {
        this.weekAmount = weekAmount;
        return this;
    }

    public BigDecimal getMonthAmount() {
        return monthAmount;
    }

    public DaySummary setMonthAmount(BigDecimal monthAmount) {
        this.monthAmount = monthAmount;
        return this;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public DaySummary setCategories(List<Category> categories) {
        this.categories = categories;
        return this;
    }
}
