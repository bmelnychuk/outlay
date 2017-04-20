package app.outlay.mvp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import app.outlay.core.data.AppPreferences;
import app.outlay.core.executor.DefaultSubscriber;
import app.outlay.domain.interactor.DeleteExpenseUseCase;
import app.outlay.domain.interactor.GetCategoriesUseCase;
import app.outlay.domain.interactor.SaveExpenseUseCase;
import app.outlay.domain.model.Expense;
import app.outlay.domain.model.Category;
import app.outlay.domain.repository.ExpenseRepository;
import app.outlay.mvp.view.EnterExpenseView;

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
        getCategoriesUseCase.execute(new DefaultSubscriber<List<Category>>() {
            @Override
            public void onNext(List<Category> categories) {
                if (getView()!=null){
                    getView().showCategories(categories);
                }
            }
        });
    }


    public void createExpense(Expense expense) {
        createExpenseUseCase.execute(expense, new DefaultSubscriber<Expense>() {
            @Override
            public void onNext(Expense expense) {
                if (getView()!=null){
                    getView().alertExpenseSuccess(expense);
                }

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