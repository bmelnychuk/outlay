package com.outlay.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.outlay.App;
import com.outlay.R;
import com.outlay.view.adapter.ExpenseAdapter;
import com.outlay.core.utils.DateUtils;
import com.outlay.domain.model.Category;
import com.outlay.domain.model.Expense;
import com.outlay.domain.model.Report;
import com.outlay.mvp.presenter.ExpensesListPresenter;
import com.outlay.mvp.view.ExpensesView;
import com.outlay.utils.IconUtils;
import com.outlay.utils.ResourceUtils;
import com.outlay.view.Navigator;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bogdan Melnychuk on 1/20/16.
 */
public class ExpensesListFragment extends BaseFragment implements ExpensesView {
    public static final String ARG_CATEGORY_ID = "_argCategoryId";
    public static final String ARG_DATE_FROM = "_argDateFrom";
    public static final String ARG_DATE_TO = "_argDateTo";


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.noResults)
    View noResults;

    @Bind(R.id.filterCategoryIcon)
    PrintView filterCategoryIcon;

    @Bind(R.id.filterCategoryName)
    TextView filterCategoryName;

    @Bind(R.id.filterDateLabel)
    TextView filterDateLabel;

    @Inject
    ExpensesListPresenter presenter;

    private ExpenseAdapter adapter;
    private Date dateFrom;
    private Date dateTo;
    private String categoryId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getUserComponent(getActivity()).inject(this);
        presenter.attachView(this);

        long dateFromMillis = getArguments().getLong(ARG_DATE_FROM);
        long dateToMillis = getArguments().getLong(ARG_DATE_TO);

        dateFrom = new Date(dateFromMillis);
        dateTo = new Date(dateToMillis);
        if (getArguments().containsKey(ARG_CATEGORY_ID)) {
            categoryId = getArguments().getString(ARG_CATEGORY_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses_list, null, false);
        ButterKnife.bind(this, view);
        enableToolbar(toolbar);
        setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.caption_expenses));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new ExpenseAdapter();
        adapter.setOnExpenseClickListener(expense -> Navigator.goToExpenseDetails(getActivity(), expense.getId()));
        recyclerView.setAdapter(adapter);
        fab.setImageDrawable(ResourceUtils.getMaterialToolbarIcon(getActivity(), R.string.ic_material_add));
        fab.setOnClickListener(v -> Navigator.goToExpenseDetails(getActivity(), null));
        filterDateLabel.setText(getDateLabel());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadExpenses(dateFrom, dateTo, categoryId);
    }

    private void displayExpenses(List<Expense> expenses) {
        if (expenses.isEmpty()) {
            noResults.setVisibility(View.VISIBLE);
        } else {
            noResults.setVisibility(View.GONE);
            adapter.setItems(expenses);
        }
    }

    private void displayCategory(Category category) {
        if (category != null) {
            IconUtils.loadCategoryIcon(category, filterCategoryIcon);
            filterCategoryName.setText(category.getTitle());
        } else {
            filterCategoryName.setVisibility(View.GONE);
            filterCategoryIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public void showReport(Report report) {
        displayExpenses(report.getExpenses());
        displayCategory(report.getCategory());
    }

    private String getDateLabel() {
        String dateFromStr = DateUtils.toShortString(dateFrom);
        String dateToStr = DateUtils.toShortString(dateTo);
        String result = dateFromStr;
        if (!dateFromStr.equals(dateToStr)) {
            result += " - " + dateToStr;
        }
        return result;
    }
}
