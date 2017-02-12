package app.outlay.firebase;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import app.outlay.BuildConfig;
import app.outlay.analytics.Analytics;
import app.outlay.core.utils.DateUtils;
import app.outlay.domain.model.Category;
import app.outlay.domain.model.Expense;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by bmelnychuk on 2/11/17.
 */

public class FirebaseAnalyticsImpl implements Analytics {
    private FirebaseAnalytics mFirebaseAnalytics;

    @Inject
    public FirebaseAnalyticsImpl(Context context) {
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    private void trackEvent(String event, Bundle params) {
        if (BuildConfig.USE_ANALYTICS) {
            mFirebaseAnalytics.logEvent(event, params);
        }
    }

    @Override
    public void trackGuestSignIn() {
        trackEvent("sign_in_guest", null);
    }

    @Override
    public void trackEmailSignIn() {
        trackEvent("sign_in_email", null);
    }

    @Override
    public void trackSignUp() {
        trackEvent("sign_up", null);
    }

    @Override
    public void trackSingOut() {
        trackEvent("sign_out", null);
    }

    @Override
    public void trackLinkAccount() {
        trackEvent("link_account", null);
    }

    @Override
    public void trackExpenseCreated(Expense e) {
        trackEvent("expense_created", expenseBundle(e));
    }

    @Override
    public void trackExpenseDeleted(Expense e) {
        trackEvent("expense_deleted", expenseBundle(e));
    }

    @Override
    public void trackExpenseUpdated(Expense e) {
        trackEvent("expense_updated", expenseBundle(e));
    }

    @Override
    public void trackCategoryCreated(Category c) {
        trackEvent("category_created", categoryBundle(c));
    }

    @Override
    public void trackCategoryDeleted(Category c) {
        trackEvent("category_deleted", categoryBundle(c));
    }

    @Override
    public void trackCategoryUpdated(Category c) {
        trackEvent("category_updated", categoryBundle(c));
    }

    @Override
    public void trackViewDailyExpenses() {
        trackEvent("expenses_daily", null);
    }

    @Override
    public void trackViewWeeklyExpenses() {
        trackEvent("expenses_weekly", null);
    }

    @Override
    public void trackViewMonthlyExpenses() {
        trackEvent("expenses_monthly", null);
    }

    @Override
    public void trackExpensesViewDateChange(Date from, Date to) {
        Bundle b = new Bundle();
        b.putLong("dateFromLong", from.getTime());
        b.putString("dateFrom", DateUtils.toShortString(from));

        b.putLong("dateToLong", to.getTime());
        b.putString("dateTo", DateUtils.toShortString(to));
        trackEvent("expenses_view_date_changed", b);
    }

    @Override
    public void trackViewExpensesList() {
        trackEvent("expenses_list_view", null);
    }

    @Override
    public void trackViewCategoriesList() {
        trackEvent("categories_list_view", null);
    }

    @Override
    public void trackCategoryDragEvent() {
        trackEvent("categories_drag_event", null);
    }

    @Override
    public void trackFeedbackClick() {
        trackEvent("feedback_click", null);
    }

    @Override
    public void trackMainScreenDateChange(Date todayDate, Date changeTo) {
        Bundle b = new Bundle();
        b.putLong("currentDateLong", todayDate.getTime());
        b.putString("currentDate", DateUtils.toShortString(todayDate));

        b.putLong("changeToLong", changeTo.getTime());
        b.putString("changeTo", DateUtils.toShortString(changeTo));
        trackEvent("main_screen_date_change", b);
    }

    @Override
    public void trackAnalysisView() {
        trackEvent("analysis_view", null);
    }

    @Override
    public void trackAnalysisPerformed(Date from, Date to) {
        Bundle b = new Bundle();
        b.putLong("dateFromLong", from.getTime());
        b.putString("dateFrom", DateUtils.toShortString(from));

        b.putLong("dateToLong", to.getTime());
        b.putString("dateTo", DateUtils.toShortString(to));
        trackEvent("analysis_event", b);
    }

    private Bundle expenseBundle(Expense e) {
        Bundle b = new Bundle();
        b.putString("categoryIcon", e.getCategory().getIcon());
        b.putString("categoryName", e.getCategory().getTitle());
        b.putString("amount", e.getAmount().toString());
        return b;
    }

    private Bundle categoryBundle(Category c) {
        Bundle b = new Bundle();
        b.putString("categoryIcon", c.getIcon());
        b.putString("categoryName", c.getTitle());
        return b;
    }
}
