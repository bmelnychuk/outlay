package com.outlay.mvp.view;

import com.outlay.domain.model.Report;

/**
 * Created by bmelnychuk on 10/25/16.
 */

public interface ExpensesView extends MvpView {
    void showReport(Report report);
}