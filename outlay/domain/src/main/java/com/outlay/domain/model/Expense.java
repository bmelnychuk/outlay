package com.outlay.domain.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class Expense {
    private Long id;
    private String note;
    private BigDecimal amount;
    private Date reportedAt;
    private Category category;

    public Long getId() {
        return id;
    }

    public Expense setId(Long id) {
        this.id = id;
        return this;
    }

    public String getNote() {
        return note;
    }

    public Expense setNote(String note) {
        this.note = note;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Expense setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Date getReportedAt() {
        return reportedAt;
    }

    public Expense setReportedAt(Date reportedAt) {
        this.reportedAt = reportedAt;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Expense setCategory(Category category) {
        this.category = category;
        return this;
    }
}
