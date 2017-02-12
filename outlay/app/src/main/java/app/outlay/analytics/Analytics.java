package app.outlay.analytics;

import app.outlay.domain.model.Category;
import app.outlay.domain.model.Expense;

import java.util.Date;

/**
 * Created by bmelnychuk on 2/11/17.
 */

public interface Analytics {
    void trackGuestSignIn();

    void trackEmailSignIn();

    void trackSignUp();

    void trackSingOut();

    void trackLinkAccount();

    void trackExpenseCreated(Expense e);

    void trackExpenseDeleted(Expense e);

    void trackExpenseUpdated(Expense e);

    void trackCategoryCreated(Category c);

    void trackCategoryDeleted(Category c);

    void trackCategoryUpdated(Category c);

    void trackCategoryDragEvent();

    void trackViewDailyExpenses();

    void trackViewWeeklyExpenses();

    void trackViewMonthlyExpenses();

    void trackExpensesViewDateChange(Date from, Date to);

    void trackViewExpensesList();

    void trackViewCategoriesList();

    void trackFeedbackClick();

    void trackMainScreenDateChange(Date todayDate, Date changeTo);

    void trackAnalysisView();

    void trackAnalysisPerformed(Date from, Date to);
}
