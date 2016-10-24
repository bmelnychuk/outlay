package com.outlay.domain.interactor;

import com.outlay.core.executor.PostExecutionThread;
import com.outlay.core.executor.ThreadExecutor;
import com.outlay.core.utils.DateUtils;
import com.outlay.domain.model.DateSummary;
import com.outlay.domain.model.Report;
import com.outlay.domain.repository.ExpenseRepository;

import java.util.Date;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class GetDateSummary extends UseCase<Date, DateSummary> {
    private ExpenseRepository expenseRepository;

    @Inject
    public GetDateSummary(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ExpenseRepository expenseRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.expenseRepository = expenseRepository;
    }

    @Override
    protected Observable<DateSummary> buildUseCaseObservable(final Date date) {
        Date startOfMonth = DateUtils.getMonthStart(date);
        Date endOfMonth = DateUtils.getMonthEnd(date);

        return expenseRepository.getExpenses(startOfMonth, endOfMonth)
                .map(expenses -> {
                    Report report = new Report();
                    report.setStartDate(startOfMonth);
                    report.setEndDate(endOfMonth);
                    report.setExpenses(expenses);
                    return report.getDateSummary(date);
                });
    }
}
