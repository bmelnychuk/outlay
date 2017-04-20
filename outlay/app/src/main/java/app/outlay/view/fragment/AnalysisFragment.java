package app.outlay.view.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import app.outlay.core.utils.DateUtils;
import app.outlay.domain.model.Category;
import app.outlay.domain.model.Expense;
import app.outlay.domain.model.Report;
import app.outlay.mvp.presenter.AnalysisPresenter;
import app.outlay.mvp.view.AnalysisView;
import app.outlay.utils.DeviceUtils;
import app.outlay.utils.IconUtils;
import app.outlay.utils.ResourceUtils;
import app.outlay.view.autocomplete.CategoryAutoCompleteAdapter;
import app.outlay.view.dialog.DatePickerFragment;
import app.outlay.view.fragment.base.BaseMvpFragment;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by bmelnychuk on 2/10/17.
 */

public class AnalysisFragment extends BaseMvpFragment<AnalysisView, AnalysisPresenter> implements AnalysisView {

    @Bind(app.outlay.R.id.categoryTitle)
    MaterialAutoCompleteTextView categoryTitle;

    @Bind(app.outlay.R.id.barChart)
    BarChart barChart;

    @Bind(app.outlay.R.id.toolbar)
    Toolbar toolbar;

    @Bind(app.outlay.R.id.categoryIcon)
    ImageView categoryIcon;

    @Bind(app.outlay.R.id.startDate)
    EditText startDateEdit;

    @Bind(app.outlay.R.id.endDate)
    EditText endDateEdit;

    @Inject
    AnalysisPresenter presenter;

    private CategoryAutoCompleteAdapter autoCompleteAdapter;
    private DayAxisValueFormatter dayAxisValueFormatter;

    private Date startDate;
    private Date endDate;
    private Category selectedCategory;

    @NonNull
    @Override
    public AnalysisPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApp().getUserComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(app.outlay.R.layout.fragment_analysis, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbar(toolbar);
        setTitle(getResources().getString(app.outlay.R.string.menu_item_analysis));
        setDisplayHomeAsUpEnabled(true);

        Drawable noCategoryIcon = IconUtils.getIconMaterialIcon(
                getContext(),
                MaterialDesignIconic.Icon.gmi_label,
                ContextCompat.getColor(getActivity(), app.outlay.R.color.icon_inactive),
                app.outlay.R.dimen.report_category_icon,
                4
        );
        categoryIcon.setImageDrawable(noCategoryIcon);
        startDateEdit.setOnClickListener(v -> showDatePickerDialog(0));
        endDateEdit.setOnClickListener(v -> showDatePickerDialog(1));

        autoCompleteAdapter = new CategoryAutoCompleteAdapter();
        categoryTitle.setAdapter(autoCompleteAdapter);
        categoryTitle.setOnItemClickListener((parent, view1, position, id) -> {
            DeviceUtils.hideKeyboard(getActivity());
            Category category = autoCompleteAdapter.getItem(position);
            selectedCategory = category;
            loadCategoryIcon(category);
            onDataChanged();
        });

        initChart();

        getPresenter().getCategories();
    }

    private void loadCategoryIcon(Category category) {
        int iconCodeRes = ResourceUtils.getIntegerResource(getContext(), category.getIcon());
        Drawable categoryIconDrawable = IconUtils.getCategoryIcon(getContext(),
                iconCodeRes,
                category.getColor(),
                app.outlay.R.dimen.report_category_icon
        );
        categoryIcon.setImageDrawable(categoryIconDrawable);
    }

    private void onDataChanged() {
        if (selectedCategory == null) {
            return;
        }
        if (startDate == null || endDate == null) {
            return;
        }
        DateTime startDateTime = new DateTime(startDate);
        DateTime endDateTime = new DateTime(endDate);

        if (startDateTime.isAfter(endDateTime)) {
            return;
        }

        analytics().trackAnalysisPerformed(startDate, endDate);
        getPresenter().getExpenses(startDate, endDate, selectedCategory);
    }

    private void initChart() {
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setMaxVisibleValueCount(60);
        barChart.getLegend().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.setScaleYEnabled(false);

        dayAxisValueFormatter = new DayAxisValueFormatter();
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(270);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(ContextCompat.getColor(getActivity(), app.outlay.R.color.icon_active));
        xAxis.setValueFormatter(dayAxisValueFormatter);


        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setValueFormatter(new AmountValueFormatter());
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setTextColor(ContextCompat.getColor(getActivity(), app.outlay.R.color.icon_active));
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);
    }

    private void showDatePickerDialog(int dateType) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener((parent, year, monthOfYear, dayOfMonth) -> {
            Calendar c = Calendar.getInstance();
            c.set(year, monthOfYear, dayOfMonth);
            Date selected = c.getTime();
            if (dateType == 0) {
                startDate = selected;
                startDateEdit.setText(DateUtils.toLongString(selected));
            } else {
                endDate = selected;
                endDateEdit.setText(DateUtils.toLongString(selected));
            }
            onDataChanged();
        });
        datePickerFragment.show(getChildFragmentManager(), "datePicker");
    }

    @Override
    public void showAnalysis(Report report) {
        List<BarEntry> barEntries = new ArrayList<>();
        List<Expense> expenses = report.getExpenses();

        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            barEntries.add(new BarEntry(
                    i,
                    expense.getAmount().floatValue())
            );
        }
        BarDataSet barSet = new BarDataSet(barEntries, "");
        barSet.setColor(selectedCategory.getColor());

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barSet);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setValueTextColor(ContextCompat.getColor(getActivity(), app.outlay.R.color.icon_active));
        data.setBarWidth(0.9f);

        dayAxisValueFormatter.setExpenses(expenses);
        barChart.setData(data);
        barChart.invalidate();
        barChart.animateY(500);
    }

    @Override
    public void setCategories(List<Category> categories) {
        autoCompleteAdapter.setItems(categories);
    }

    public static class DayAxisValueFormatter implements IAxisValueFormatter {
        private List<Expense> expenses;

        public DayAxisValueFormatter setExpenses(List<Expense> expenses) {
            this.expenses = expenses;
            return this;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (expenses != null && value < expenses.size()) {
                Expense expense = expenses.get((int) value);
                return DateUtils.toShortString(expense.getReportedWhen());
            } else {
                return null;
            }
        }
    }

    public static class AmountValueFormatter implements IAxisValueFormatter {
        private DecimalFormat mFormat;

        public AmountValueFormatter() {
            mFormat = new DecimalFormat("###,###,###,##0.0");
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(value);
        }

    }
}
