package com.outlay.presenter;

import com.outlay.core.executor.DefaultSubscriber;
import com.outlay.domain.interactor.CreateExpenseUseCase;
import com.outlay.domain.interactor.GetDateSummary;
import com.outlay.domain.interactor.InitUseCase;
import com.outlay.domain.model.DateSummary;
import com.outlay.preferences.PreferencesManager;
import com.outlay.view.fragment.MainFragment;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Bogdan Melnychuk on 1/25/16.
 */
public class MainFragmentPresenter {
    private MainFragment view;
    private InitUseCase initUseCase;
    private CreateExpenseUseCase createExpenseUseCase;
    private GetDateSummary getDateSummaryUseCase;

    @Inject
    public MainFragmentPresenter(
            InitUseCase initUseCase,
            CreateExpenseUseCase createExpenseUseCase,
            GetDateSummary getDateSummaryUseCase
    ) {
        this.initUseCase = initUseCase;
        this.createExpenseUseCase = createExpenseUseCase;
        this.getDateSummaryUseCase = getDateSummaryUseCase;
    }

    public void attachView(MainFragment fragment) {
        this.view = fragment;
    }

    public void loadCategories() {
        initUseCase.execute(new DefaultSubscriber<List<com.outlay.domain.model.Category>>() {
            @Override
            public void onNext(List<com.outlay.domain.model.Category> categories) {
                view.displayCategories(categories);
            }
        });
    }

    public void loadSummary(Date date) {
        getDateSummaryUseCase.execute(date, new DefaultSubscriber<DateSummary>() {
            @Override
            public void onNext(DateSummary dateSummary) {
                view.displaySummary(dateSummary);
            }
        });
    }


    public void insertExpense(com.outlay.domain.model.Expense expense) {
        createExpenseUseCase.execute(expense, new DefaultSubscriber() {

        });
    }
}
