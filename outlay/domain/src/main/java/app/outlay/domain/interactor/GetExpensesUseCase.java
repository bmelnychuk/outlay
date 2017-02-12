package app.outlay.domain.interactor;

import app.outlay.core.executor.PostExecutionThread;
import app.outlay.core.executor.ThreadExecutor;
import app.outlay.domain.model.Report;
import app.outlay.domain.repository.CategoryRepository;
import app.outlay.domain.repository.ExpenseRepository;

import java.util.Date;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class GetExpensesUseCase extends UseCase<GetExpensesUseCase.Input, Report> {
    private ExpenseRepository expenseRepository;
    private CategoryRepository categoryRepository;

    @Inject
    public GetExpensesUseCase(
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
    protected Observable<Report> buildUseCaseObservable(GetExpensesUseCase.Input input) {
//        if (input.categoryId != null) {
//            return Observable.zip(
//                    categoryRepository.getById(input.categoryId),
//                    expenseRepository.getExpenses(input.startDate, input.endDate, input.categoryId),
//                    (category, expenses) -> {
//                        Report report = new Report();
//                        report.setEndDate(input.endDate);
//                        report.setStartDate(input.startDate);
//                        report.setExpenses(expenses);
//                        report.setCategory(category);
//                        return report;
//                    }
//            );
//        } else {
            return expenseRepository.getExpenses(input.startDate, input.endDate, input.categoryId)
                    .map(expenses -> {
                        Report report = new Report();
                        report.setEndDate(input.endDate);
                        report.setStartDate(input.startDate);
                        report.setExpenses(expenses);
                        return report;
                    });
//        }
    }

    public static class Input {
        private final Date startDate;
        private final Date endDate;
        private final String categoryId;

        public Input(Date startDate, Date endDate, String categoryId) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.categoryId = categoryId;
        }
    }
}
