package com.outlay.mvp.presenter;

import com.outlay.core.executor.DefaultSubscriber;
import com.outlay.domain.interactor.DeleteExpenseUseCase;
import com.outlay.domain.interactor.GetCategoriesUseCase;
import com.outlay.domain.interactor.GetExpenseUseCase;
import com.outlay.domain.interactor.SaveExpenseUseCase;
import com.outlay.domain.model.Category;
import com.outlay.domain.model.Expense;
import com.outlay.mvp.view.ExpenseDetailsView;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class ExpensesDetailsPresenter extends MvpPresenter<ExpenseDetailsView> {
    private GetCategoriesUseCase getCategoriesUseCase;
    private GetExpenseUseCase getExpenseUseCase;
    private SaveExpenseUseCase saveExpenseUseCase;
    private DeleteExpenseUseCase deleteExpenseUseCase;

    @Inject
    public ExpensesDetailsPresenter(
            GetCategoriesUseCase getCategoriesUseCase,
            GetExpenseUseCase getExpenseUseCase,
            SaveExpenseUseCase saveExpenseUseCase,
            DeleteExpenseUseCase deleteExpenseUseCase
    ) {
        this.getCategoriesUseCase = getCategoriesUseCase;
        this.getExpenseUseCase = getExpenseUseCase;
        this.saveExpenseUseCase = saveExpenseUseCase;
        this.deleteExpenseUseCase = deleteExpenseUseCase;
    }

    public void loadExpense(String expenseId, Date date) {
        getExpenseUseCase.execute(new GetExpenseUseCase.Input(expenseId, date), new DefaultSubscriber<Expense>() {
            @Override
            public void onNext(Expense expense) {
                getView().showExpense(expense);
            }
        });

    }

    public void loadCategories() {
        getCategoriesUseCase.execute(new DefaultSubscriber<List<Category>>() {
            @Override
            public void onNext(List<Category> categories) {
                super.onNext(categories);
                getView().showCategories(categories);
            }
        });
    }

    public void updateExpense(Expense expense) {
        saveExpenseUseCase.execute(expense, new DefaultSubscriber());
    }

    public void deleteExpense(Expense expense) {
        deleteExpenseUseCase.execute(expense, new DefaultSubscriber());
    }
}
