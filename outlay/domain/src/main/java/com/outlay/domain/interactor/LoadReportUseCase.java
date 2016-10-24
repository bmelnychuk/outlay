package com.outlay.domain.interactor;

import com.outlay.core.executor.PostExecutionThread;
import com.outlay.core.executor.ThreadExecutor;
import com.outlay.domain.model.Period;
import com.outlay.domain.model.Report;
import com.outlay.domain.repository.ExpenseRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class LoadReportUseCase extends UseCase<Period, Report> {
    private ExpenseRepository expenseRepository;

    @Inject
    public LoadReportUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ExpenseRepository expenseRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.expenseRepository = expenseRepository;
    }

    @Override
    protected Observable<Report> buildUseCaseObservable(Period period) {
        return expenseRepository.getExpenses(period.getStartDate(), period.getEndDate())
                .map(expenses -> {
                    Report report = new Report();
                    report.setEndDate(period.getEndDate());
                    report.setStartDate(period.getStartDate());
                    report.setExpenses(expenses);
                    return report;
                });
    }
}
