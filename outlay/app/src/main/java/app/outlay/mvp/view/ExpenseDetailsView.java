package app.outlay.mvp.view;

import com.hannesdorfmann.mosby.mvp.MvpView;
import app.outlay.domain.model.Category;
import app.outlay.domain.model.Expense;

import java.util.List;

/**
 * Created by bmelnychuk on 10/25/16.
 */

public interface ExpenseDetailsView extends MvpView {
    void showExpense(Expense category);
    void showCategories(List<Category> categoryList);
}
