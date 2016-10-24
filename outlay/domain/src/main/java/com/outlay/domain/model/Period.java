package com.outlay.domain.model;

import java.util.Date;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class Period {
    private Date startDate;
    private Date endDate;

    public Period(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Period setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Period setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }
}
