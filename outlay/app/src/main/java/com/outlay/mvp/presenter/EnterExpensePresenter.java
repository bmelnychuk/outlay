package com.outlay.mvp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.outlay.core.data.AppPreferences;
import com.outlay.core.executor.DefaultSubscriber;
import com.outlay.domain.interactor.DeleteExpenseUseCase;
import com.outlay.domain.interactor.GetCategoriesUseCase;
import com.outlay.domain.interactor.SaveExpenseUseCase;
import com.outlay.domain.model.Expense;
import com.outlay.domain.repository.ExpenseRepository;
import com.outlay.mvp.view.EnterExpenseView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Bogdan Melnychuk on 1/25/16.
 */
public class EnterExpensePresenter extends MvpBasePresenter<EnterExpenseView> {
    private GetCategoriesUseCase getCategoriesUseCase;
    private SaveExpenseUseCase createExpenseUseCase;
    private DeleteExpenseUseCase deleteExpenseUseCase;
    private AppPreferences appPreferences;

    @Inject
    public EnterExpensePresenter(
            GetCategoriesUseCase getCategoriesUseCase,
            SaveExpenseUseCase createExpenseUseCase,
            DeleteExpenseUseCase deleteExpenseUseCase,
            AppPreferences appPreferences,
            ExpenseRepository repository
    ) {
        this.getCategoriesUseCase = getCategoriesUseCase;
        this.createExpenseUseCase = createExpenseUseCase;
        this.deleteExpenseUseCase = deleteExpenseUseCase;
        this.appPreferences = appPreferences;
    }

    public void getCategories() {
        getCategoriesUseCase.execute(new DefaultSubscriber<List<com.outlay.domain.model.Category>>() {
            @Override
            public void onNext(List<com.outlay.domain.model.Category> categories) {
                getView().showCategories(categories);
            }
        });
    }


    public void createExpense(Expense expense) {
        createExpenseUseCase.execute(expense, new DefaultSubscriber<Expense>() {
            @Override
            public void onNext(Expense expense) {
                getView().alertExpenseSuccess(expense);

            }
        });
    }

    public void deleteExpense(Expense expense) {
        deleteExpenseUseCase.execute(expense, new DefaultSubscriber<Expense>() {
            @Override
            public void onCompleted() {
            }
        });
    }
}