package com.outlay.mvp.presenter;

import com.outlay.core.executor.DefaultSubscriber;
import com.outlay.core.utils.DateUtils;
import com.outlay.domain.interactor.LoadReportUseCase;
import com.outlay.domain.model.ExpensePeriod;
import com.outlay.domain.model.Report;
import com.outlay.mvp.view.StatisticView;
import com.outlay.view.Page;
import com.outlay.view.fragment.ReportFragment;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class ReportPresenter extends MvpPresenter<StatisticView> {
    private LoadReportUseCase loadReportUseCase;

    @Inject
    public ReportPresenter(
            LoadReportUseCase loadReportUseCase
    ) {
        this.loadReportUseCase = loadReportUseCase;
    }

    public void loadReport(Date date, int period) {
        Date startDate = date;
        Date endDate = date;

        switch (period) {
            case ReportFragment.PERIOD_DAY:
                startDate = DateUtils.getDayStart(date);
                endDate = DateUtils.getDayEnd(date);
                break;
            case ReportFragment.PERIOD_WEEK:
                startDate = DateUtils.getWeekStart(date);
                endDate = DateUtils.getWeekEnd(date);
                break;
            case ReportFragment.PERIOD_MONTH:
                startDate = DateUtils.getMonthStart(date);
                endDate = DateUtils.getMonthEnd(date);
                break;
        }


        loadReportUseCase.execute(new ExpensePeriod(startDate, endDate), new DefaultSubscriber<Report>() {
            @Override
            public void onNext(Report report) {
                super.onNext(report);
                getView().showReports(new ArrayList<>(report.groupByCategory().values()));
            }
        });
    }
}