package app.outlay.mvp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import app.outlay.core.executor.DefaultSubscriber;
import app.outlay.core.utils.DateUtils;
import app.outlay.domain.interactor.GetExpensesUseCase;
import app.outlay.domain.model.Report;
import app.outlay.mvp.view.StatisticView;
import app.outlay.view.fragment.ReportFragment;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class ReportPresenter extends MvpBasePresenter<StatisticView> {
    private GetExpensesUseCase loadReportUseCase;

    @Inject
    public ReportPresenter(
            GetExpensesUseCase loadReportUseCase
    ) {
        this.loadReportUseCase = loadReportUseCase;
    }

    public void getExpenses(Date date, int period) {
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


        loadReportUseCase.execute(new GetExpensesUseCase.Input(startDate, endDate, null), new DefaultSubscriber<Report>() {
            @Override
            public void onNext(Report report) {
                super.onNext(report);
                if (getView()!=null){
                    getView().showReport(report);
                }
            }
        });
    }
}