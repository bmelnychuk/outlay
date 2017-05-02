package app.outlay.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.*;
import app.outlay.core.utils.DateUtils;
import app.outlay.domain.model.Report;
import app.outlay.mvp.presenter.ReportPresenter;
import app.outlay.mvp.view.StatisticView;
import app.outlay.utils.ResourceUtils;
import app.outlay.view.Navigator;
import app.outlay.view.adapter.ReportAdapter;
import app.outlay.view.dialog.DatePickerFragment;
import app.outlay.view.fragment.base.BaseMvpFragment;
import app.outlay.view.helper.OnTabSelectedListenerAdapter;
import app.outlay.view.model.CategorizedExpenses;
import butterknife.Bind;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bogdan Melnychuk on 1/20/16.
 */
public class ReportFragment extends BaseMvpFragment<StatisticView, ReportPresenter> implements StatisticView {
    public static final String ARG_DATE = "_argDate";

    public static final int PERIOD_DAY = 0;
    public static final int PERIOD_WEEK = 1;
    public static final int PERIOD_MONTH = 2;

    @Bind(app.outlay.R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(app.outlay.R.id.tabs)
    TabLayout tabLayout;

    @Bind(app.outlay.R.id.toolbar)
    Toolbar toolbar;

    @Bind(app.outlay.R.id.noResults)
    View noResults;

    private int selectedPeriod;
    private Date selectedDate;
    private ReportAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApp().getUserComponent().inject(this);
        selectedDate = new Date(getArguments().getLong(ARG_DATE, new Date().getTime()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(app.outlay.R.layout.fragment_report, null, false);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(app.outlay.R.menu.menu_report, menu);
        MenuItem dateItem = menu.findItem(app.outlay.R.id.action_date);
        dateItem.setIcon(ResourceUtils.getMaterialToolbarIcon(getActivity(), app.outlay.R.string.ic_material_today));

        MenuItem listItem = menu.findItem(app.outlay.R.id.action_list);
        listItem.setIcon(ResourceUtils.getMaterialToolbarIcon(getActivity(), app.outlay.R.string.ic_material_list));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case app.outlay.R.id.action_date:
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setOnDateSetListener((view, year, monthOfYear, dayOfMonth) -> {
                    Calendar c = Calendar.getInstance();
                    c.set(year, monthOfYear, dayOfMonth);
                    Date selected = c.getTime();
                    analytics().trackExpensesViewDateChange(selectedDate, selected);
                    selectedDate = selected;
                    ReportFragment.this.setTitle(DateUtils.toShortString(selected));
                    updateTitle();
                    getPresenter().getExpenses(selectedDate, selectedPeriod);
                });
                datePickerFragment.show(getChildFragmentManager(), "datePicker");
                break;
            case app.outlay.R.id.action_list:
                analytics().trackViewExpensesList();
                goToExpensesList(selectedDate, selectedPeriod);
            default:
                break;
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

        tabLayout.addTab(tabLayout.newTab().setText(app.outlay.R.string.label_day));
        tabLayout.addTab(tabLayout.newTab().setText(app.outlay.R.string.label_week));
        tabLayout.addTab(tabLayout.newTab().setText(app.outlay.R.string.label_month));
        tabLayout.getTabAt(selectedPeriod).select();
        tabLayout.setOnTabSelectedListener(new OnTabSelectedListenerAdapter() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedPeriod = tab.getPosition();
                switch (selectedPeriod) {
                    case ReportFragment.PERIOD_DAY:
                        analytics().trackViewDailyExpenses();
                        break;
                    case ReportFragment.PERIOD_WEEK:
                        analytics().trackViewWeeklyExpenses();
                        break;
                    case ReportFragment.PERIOD_MONTH:
                        analytics().trackViewMonthlyExpenses();
                        break;
                    default:
                        break;
                }
                updateTitle();
                getPresenter().getExpenses(selectedDate, selectedPeriod);
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReportAdapter();
        recyclerView.setAdapter(adapter);
        getPresenter().getExpenses(selectedDate, selectedPeriod);

        adapter.setOnItemClickListener((category, report) -> goToExpensesList(selectedDate, selectedPeriod, category.getId()));
    }

    @Override
    public void showReport(Report report) {
        if (report.isEmpty()) {
            noResults.setVisibility(View.VISIBLE);
        } else {
            noResults.setVisibility(View.GONE);
            adapter.setItems(new CategorizedExpenses(report));
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
            default:
                break;
        }
    }

    public void goToExpensesList(Date date, int selectedPeriod) {
        this.goToExpensesList(date, selectedPeriod, null);
    }

    public void goToExpensesList(Date date, int selectedPeriod, String category) {
        Date filledDate = DateUtils.fillCurrentTime(date);
        Date startDate;
        Date endDate;

        switch (selectedPeriod) {
            case ReportFragment.PERIOD_DAY:
                startDate = DateUtils.getDayStart(filledDate);
                endDate = DateUtils.getDayEnd(filledDate);
                break;
            case ReportFragment.PERIOD_WEEK:
                startDate = DateUtils.getWeekStart(filledDate);
                endDate = DateUtils.getWeekEnd(filledDate);
                break;
            case ReportFragment.PERIOD_MONTH:
                startDate = DateUtils.getMonthStart(filledDate);
                endDate = DateUtils.getMonthEnd(filledDate);
                break;
            default:
                startDate = filledDate;
                endDate = filledDate;
                break;
        }
        Navigator.goToExpensesList(getActivity(), startDate, endDate, category);
    }
}