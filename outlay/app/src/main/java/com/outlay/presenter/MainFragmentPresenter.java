package com.outlay.presenter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.outlay.R;
import com.outlay.api.OutlayDatabaseApi;
import com.outlay.dao.Category;
import com.outlay.dao.Expense;
import com.outlay.model.Icon;
import com.outlay.preferences.PreferencesManager;
import com.outlay.view.fragment.MainFragment;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Bogdan Melnychuk on 1/25/16.
 */
public class MainFragmentPresenter {
    private OutlayDatabaseApi api;
    private MainFragment view;
    private PreferencesManager preferencesManager;

    @Inject
    public MainFragmentPresenter(OutlayDatabaseApi outlayDatabaseApi, PreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
        this.api = outlayDatabaseApi;
    }

    public void attachView(MainFragment fragment) {
        this.view = fragment;
    }

    public void loadCategories() {
        if (preferencesManager.isFirstRun()) {
            List<Category> defaultCategories = getDefault(view.getActivity());
            api.insertCategories(defaultCategories)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        preferencesManager.putFirstRun(false);
                        view.displayCategories(response);
                    });
        } else {
            api.getCategories()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> view.displayCategories(response));
        }


    }

    public void loadSummary(Date date) {
        api.getSummary(date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> view.displaySummary(response));
    }


    public void insertExpense(Expense expense) {
        DateTime dateTime = new DateTime(expense.getReportedAt().getTime());
        dateTime = dateTime.withTime(LocalTime.now());
        expense.setReportedAt(dateTime.toDate());
        api.insertExpense(expense);
    }

    public static List<Category> getDefault(Context context) {
        List<Category> result = new ArrayList<>();
        result.add(category(context.getString(R.string.category_car), "ic_cars", ContextCompat.getColor(context, R.color.blue), 0));
        result.add(category(context.getString(R.string.category_house), "ic_house", ContextCompat.getColor(context, R.color.red), 1));
        result.add(category(context.getString(R.string.category_grocery), "ic_shopping", ContextCompat.getColor(context, R.color.green), 2));
        result.add(category(context.getString(R.string.category_games), "ic_controller", ContextCompat.getColor(context, R.color.purple), 3));
        result.add(category(context.getString(R.string.category_clothes), "ic_t_shirt", ContextCompat.getColor(context, R.color.teal), 4));
        result.add(category(context.getString(R.string.category_tickets), "ic_tag", ContextCompat.getColor(context, R.color.amber), 5));
        result.add(category(context.getString(R.string.category_sport), "ic_weightlifting", ContextCompat.getColor(context, R.color.brown), 6));
        result.add(category(context.getString(R.string.category_travel), "ic_flight", ContextCompat.getColor(context, R.color.cyan), 7));
        return result;
    }

    private static Category category(String title, String icon, int color, int order) {
        Category c = new Category();
        c.setTitle(title);
        c.setIcon(icon);
        c.setColor(color);
        c.setOrder(order);
        return c;
    }
}
