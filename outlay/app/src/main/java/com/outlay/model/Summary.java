package com.outlay.model;

import com.outlay.dao.Category;

import java.util.Date;
import java.util.List;

/**
 * Created by Bogdan Melnychuk on 1/30/16.
 */
public class Summary {
    private Date date;
    private double dayAmount;
    private double weekAmount;
    private double monthAmount;
    private List<Category> categories;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getDayAmount() {
        return dayAmount;
    }

    public void setDayAmount(double dayAmount) {
        this.dayAmount = dayAmount;
    }

    public double getWeekAmount() {
        return weekAmount;
    }

    public void setWeekAmount(double weekAmount) {
        this.weekAmount = weekAmount;
    }

    public double getMonthAmount() {
        return monthAmount;
    }

    public void setMonthAmount(double monthAmount) {
        this.monthAmount = monthAmount;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
