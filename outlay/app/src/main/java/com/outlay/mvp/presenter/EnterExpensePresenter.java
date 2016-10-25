package com.outlay.mvp.presenter;

import com.outlay.core.executor.DefaultSubscriber;
import com.outlay.domain.interactor.DeleteExpenseUseCase;
import com.outlay.domain.interactor.GetDateSummary;
import com.outlay.domain.interactor.InitUseCase;
import com.outlay.domain.interactor.SaveExpenseUseCase;
import com.outlay.domain.model.DateSummary;
import com.outlay.domain.model.Expense;
import com.outlay.mvp.view.EnterExpenseView;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Bogdan Melnychuk on 1/25/16.
 */
public class EnterExpensePresenter extends MvpPresenter<EnterExpenseView> {
    private InitUseCase initUseCase;
    private SaveExpenseUseCase createExpenseUseCase;
    private GetDateSummary getDateSummaryUseCase;
    private DeleteExpenseUseCase deleteExpenseUseCase;

    @Inject
    public EnterExpensePresenter(
            InitUseCase initUseCase,
            SaveExpenseUseCase createExpenseUseCase,
            GetDateSummary getDateSummaryUseCase,
            DeleteExpenseUseCase deleteExpenseUseCase
    ) {
        this.initUseCase = initUseCase;
        this.createExpenseUseCase = createExpenseUseCase;
        this.getDateSummaryUseCase = getDateSummaryUseCase;
        this.deleteExpenseUseCase = deleteExpenseUseCase;
    }

    public void loadCategories() {
        initUseCase.execute(new DefaultSubscriber<List<com.outlay.domain.model.Category>>() {
            @Override
            public void onNext(List<com.outlay.domain.model.Category> categories) {
                getView().showCategories(categories);
            }
        });
    }

    public void loadSummary(Date date) {
        getDateSummaryUseCase.execute(date, new DefaultSubscriber<DateSummary>() {
            @Override
            public void onNext(DateSummary dateSummary) {
                getView().showDateSummary(dateSummary);
            }
        });
    }


    public void insertExpense(Expense expense) {
        createExpenseUseCase.execute(expense, new DefaultSubscriber<Expense>() {
            @Override
            public void onNext(Expense expense) {
                loadSummary(new Date());
                getView().alertExpenseSuccess(expense);

            }
        });
    }

    public void deleteExpense(Expense expense) {
        deleteExpenseUseCase.execute(expense, new DefaultSubscriber<Expense>() {
            @Override
            public void onNext(Expense expense) {
                loadSummary(new Date());
            }
        });
    }
}