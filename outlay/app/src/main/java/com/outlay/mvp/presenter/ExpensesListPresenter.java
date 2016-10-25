package com.outlay.mvp.presenter;

import com.outlay.core.executor.DefaultSubscriber;
import com.outlay.domain.interactor.LoadReportUseCase;
import com.outlay.domain.model.ExpensePeriod;
import com.outlay.domain.model.Report;
import com.outlay.mvp.view.ExpensesView;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class ExpensesListPresenter extends MvpPresenter<ExpensesView> {
    private LoadReportUseCase loadReportUseCase;

    @Inject
    public ExpensesListPresenter(
            LoadReportUseCase loadReportUseCase
    ) {
        this.loadReportUseCase = loadReportUseCase;
    }

    public void loadExpenses(Date dateFrom, Date dateTo, Long categoryId) {
        ExpensePeriod period = new ExpensePeriod(dateFrom, dateTo, categoryId);
        loadReportUseCase.execute(period, new DefaultSubscriber<Report>() {
            @Override
            public void onNext(Report report) {
                getView().showReport(report);
            }
        });
    }
}
