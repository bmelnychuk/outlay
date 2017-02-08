package com.outlay.mvp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.outlay.core.executor.DefaultSubscriber;
import com.outlay.domain.interactor.GetExpensesUseCase;
import com.outlay.domain.model.Report;
import com.outlay.mvp.view.ExpensesView;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class ExpensesListPresenter extends MvpBasePresenter<ExpensesView> {
    private GetExpensesUseCase loadReportUseCase;

    @Inject
    public ExpensesListPresenter(
            GetExpensesUseCase loadReportUseCase
    ) {
        this.loadReportUseCase = loadReportUseCase;
    }

    public void findExpenses(Date dateFrom, Date dateTo, String categoryId) {
        GetExpensesUseCase.Input input = new GetExpensesUseCase.Input(dateFrom, dateTo, categoryId);
        loadReportUseCase.execute(input, new DefaultSubscriber<Report>() {
            @Override
            public void onNext(Report report) {
                getView().showReport(report);
            }
        });
    }
}
