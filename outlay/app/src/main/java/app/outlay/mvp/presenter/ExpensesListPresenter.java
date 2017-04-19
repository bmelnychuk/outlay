package app.outlay.mvp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import app.outlay.core.executor.DefaultSubscriber;
import app.outlay.domain.interactor.GetExpensesUseCase;
import app.outlay.domain.model.Report;
import app.outlay.mvp.view.ExpensesView;

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
                if (getView()!=null){
                    getView().showReport(report);
                }
            }
        });
    }
}
