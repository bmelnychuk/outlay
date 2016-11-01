package com.outlay.firebase.dto;

/**
 * Created by bmelnychuk on 10/27/16.
 */

public class ExpenseDto {
    private String id;
    private String note;
    private String amount;
    private Long reportedAt;
    private String categoryId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Long getReportedAt() {
        return reportedAt;
    }

    public void setReportedAt(Long reportedAt) {
        this.reportedAt = reportedAt;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
