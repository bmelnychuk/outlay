package com.outlay.presenter;

import com.outlay.api.OutlayDatabaseApi;
import com.outlay.core.executor.DefaultSubscriber;
import com.outlay.core.utils.DateUtils;
import com.outlay.domain.interactor.LoadReportUseCase;
import com.outlay.domain.model.Period;
import com.outlay.domain.model.Report;
import com.outlay.view.Page;
import com.outlay.view.fragment.ReportFragment;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class ReportPresenter {
    private ReportFragment view;
    private LoadReportUseCase loadReportUseCase;

    @Inject
    public ReportPresenter(
            OutlayDatabaseApi outlayDatabaseApi,
            LoadReportUseCase loadReportUseCase
    ) {
        this.loadReportUseCase = loadReportUseCase;
    }

    public void attachView(ReportFragment fragment) {
        this.view = fragment;
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


        loadReportUseCase.execute(new Period(startDate, endDate), new DefaultSubscriber<Report>() {
            @Override
            public void onNext(Report report) {
                super.onNext(report);
                view.displayReports(new ArrayList<>(report.groupByCategory().values()));
            }
        });

//        api.getExpenses(startDate, endDate)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(expenses -> {
//                    //TODO Don't like this map here, this logic should be on database side, though I am too lazy now
//                    Map<String, Report> reportMap = new TreeMap<>((lhs, rhs) -> {
//                        if (lhs == rhs) {
//                            return 0;
//                        }
//                        if (lhs == null) {
//                            return -1;
//                        }
//                        if (rhs == null) {
//                            return 1;
//                        }
//                        return lhs.compareTo(rhs);
//                    });
//                    for (Expense e : expenses) {
//                        Report r = reportMap.get(e.getCategory().getTitle());
//                        if (r == null) {
//                            r = new Report();
//                        }
//                        r.addExpense(e);
//                        reportMap.put(e.getCategory().getTitle(), r);
//                    }
//                    view.displayReports(new ArrayList<>(reportMap.values()));
//
//                });
    }

    public void goToExpensesList(Date date, int selectedPeriod) {
        this.goToExpensesList(date, selectedPeriod, null);
    }

    public void goToExpensesList(Date date, int selectedPeriod, Long category) {
        date = DateUtils.fillCurrentTime(date);
        Date startDate = date;
        Date endDate = date;

        switch (selectedPeriod) {
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
        Page.goToExpensesList(view.getActivity(), startDate, endDate, category);
    }
}