package com.outlay.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.outlay.R;
import com.outlay.core.utils.DateUtils;
import com.outlay.domain.model.Report;
import com.outlay.mvp.presenter.ReportPresenter;
import com.outlay.mvp.view.StatisticView;
import com.outlay.utils.ResourceUtils;
import com.outlay.view.Navigator;
import com.outlay.view.adapter.ReportAdapter;
import com.outlay.view.dialog.DatePickerFragment;
import com.outlay.view.fragment.base.BaseMvpFragment;
import com.outlay.view.helper.OnTabSelectedListenerAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by Bogdan Melnychuk on 1/20/16.
 */
public class ReportFragment extends BaseMvpFragment<StatisticView, ReportPresenter> implements StatisticView {
    public static final String ARG_DATE = "_argDate";

    public static final int PERIOD_DAY = 0;
    public static final int PERIOD_WEEK = 1;
    public static final int PERIOD_MONTH = 2;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.tabs)
    TabLayout tabLayout;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.noResults)
    View noResults;

    @Inject
    ReportPresenter presenter;

    private int selectedPeriod;
    private Date selectedDate;
    private ReportAdapter adapter;

    @Override
    public ReportPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApp().getUserComponent().inject(this);
        selectedDate = new Date(getArguments().getLong(ARG_DATE, new Date().getTime()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, null, false);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_report, menu);
        MenuItem dateItem = menu.findItem(R.id.action_date);
        dateItem.setIcon(ResourceUtils.getMaterialToolbarIcon(getActivity(), R.string.ic_material_today));

        MenuItem listItem = menu.findItem(R.id.action_list);
        listItem.setIcon(ResourceUtils.getMaterialToolbarIcon(getActivity(), R.string.ic_material_list));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_date:
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setOnDateSetListener((view, year, monthOfYear, dayOfMonth) -> {
                    Calendar c = Calendar.getInstance();
                    c.set(year, monthOfYear, dayOfMonth);
                    Date selected = c.getTime();
                    selectedDate = selected;
                    ReportFragment.this.setTitle(DateUtils.toShortString(selected));
                    updateTitle();
                    presenter.loadReport(selectedDate, selectedPeriod);
                });
                datePickerFragment.show(getChildFragmentManager(), "datePicker");
                break;
            case R.id.action_list:
                goToExpensesList(selectedDate, selectedPeriod);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolbar(toolbar);
        setDisplayHomeAsUpEnabled(true);
        updateTitle();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_day));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_week));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_month));
        tabLayout.getTabAt(selectedPeriod).select();
        tabLayout.setOnTabSelectedListener(new OnTabSelectedListenerAdapter() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedPeriod = tab.getPosition();
                updateTitle();
                presenter.loadReport(selectedDate, selectedPeriod);
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReportAdapter();
        recyclerView.setAdapter(adapter);
        presenter.loadReport(selectedDate, selectedPeriod);

        adapter.setOnItemClickListener(report -> goToExpensesList(selectedDate, selectedPeriod, report.getCategory().getId()));
    }

    @Override
    public void showReports(List<Report> reportList) {
        if (reportList.isEmpty()) {
            noResults.setVisibility(View.VISIBLE);
        } else {
            noResults.setVisibility(View.GONE);
            adapter.setItems(reportList);
        }
    }

    private void updateTitle() {
        switch (selectedPeriod) {
            case PERIOD_DAY:
                setTitle(DateUtils.toShortString(selectedDate));
                break;
            case PERIOD_WEEK:
                Date startDate = DateUtils.getWeekStart(selectedDate);
                Date endDate = DateUtils.getWeekEnd(selectedDate);
                setTitle(DateUtils.toShortString(startDate) + " - " + DateUtils.toShortString(endDate));
                break;
            case PERIOD_MONTH:
                startDate = DateUtils.getMonthStart(selectedDate);
                endDate = DateUtils.getMonthEnd(selectedDate);
                setTitle(DateUtils.toShortString(startDate) + " - " + DateUtils.toShortString(endDate));
                break;
        }
    }

    public void goToExpensesList(Date date, int selectedPeriod) {
        this.goToExpensesList(date, selectedPeriod, null);
    }

    public void goToExpensesList(Date date, int selectedPeriod, String category) {
        date = DateUtils.fillCurrentTime(date);
        Date startDate = date;
        Date endDate = date;

        switch (selectedPeriod) {
            case ReportFragment.PERIOD_DAY:
                startDate = DateUtils.getDayStart(date);
                endDate = DateUtils.getDayEnd(date);
                break;
            case ReportFragment.PERIOD_WEEK:
                startDate = DateUtils.getWeekStart(date);
                endDate = DateUtils.getWeekEnd(date);
                break;
            case ReportFragment.PERIOD_MONTH:
                startDate = DateUtils.getMonthStart(date);
                endDate = DateUtils.getMonthEnd(date);
                break;
        }
        Navigator.goToExpensesList(getActivity(), startDate, endDate, category);
    }
}