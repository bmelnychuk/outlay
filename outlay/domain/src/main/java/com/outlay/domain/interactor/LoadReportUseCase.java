package com.outlay.domain.interactor;

import com.outlay.core.executor.PostExecutionThread;
import com.outlay.core.executor.ThreadExecutor;
import com.outlay.domain.model.ExpensePeriod;
import com.outlay.domain.model.Report;
import com.outlay.domain.repository.CategoryRepository;
import com.outlay.domain.repository.ExpenseRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class LoadReportUseCase extends UseCase<ExpensePeriod, Report> {
    private ExpenseRepository expenseRepository;
    private CategoryRepository categoryRepository;

    @Inject
    public LoadReportUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ExpenseRepository expenseRepository,
            CategoryRepository categoryRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    protected Observable<Report> buildUseCaseObservable(ExpensePeriod period) {
        if (period.getCategoryId() != null) {
            return Observable.zip(
                    categoryRepository.getById(period.getCategoryId()),
                    expenseRepository.getExpenses(period.getStartDate(), period.getEndDate(), period.getCategoryId()),
                    (category, expenses) -> {
                        Report report = new Report();
                        report.setEndDate(period.getEndDate());
                        report.setStartDate(period.getStartDate());
                        report.setExpenses(expenses);
                        report.setCategory(category);
                        return report;
                    }
            );
        } else {
            return expenseRepository.getExpenses(period.getStartDate(), period.getEndDate(), period.getCategoryId())
                    .map(expenses -> {
                        Report report = new Report();
                        report.setEndDate(period.getEndDate());
                        report.setStartDate(period.getStartDate());
                        report.setExpenses(expenses);
                        return report;
                    });
        }
    }
}
