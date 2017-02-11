package com.outlay.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.outlay.R;
import com.outlay.domain.model.Report;
import com.outlay.mvp.presenter.AnalysisPresenter;
import com.outlay.mvp.view.AnalysisView;
import com.outlay.view.fragment.base.BaseMvpFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by bmelnychuk on 2/10/17.
 */

public class AnalysisFragment extends BaseMvpFragment<AnalysisView, AnalysisPresenter> implements AnalysisView {

    @Bind(R.id.barChart)
    BarChart barChart;

    @Inject
    AnalysisPresenter presenter;

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
        View view = inflater.inflate(R.layout.fragment_analysis, null, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(getResources().getString(R.string.menu_item_analysis));

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);

        barChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        barChart.setMaxVisibleValueCount(60);
        barChart.getLegend().setEnabled(false);
        barChart.setDrawGridBackground(false);


        IAxisValueFormatter xAxisFormatter = new MyAxisValueFormatter();

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(90);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new DayAxisValueFormatter();

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        barChart.getAxisRight().setEnabled(false);
        setData(8, 50);
    }

    @Override
    public void showAnalysis(Report report) {

    }

    private void setData(int count, float range) {

        List<BarEntry> yVals1 = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            yVals1.add(new BarEntry(i + 3, i * 10, "Label " + i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "The year 2017");
        set1.setColors(ColorTemplate.MATERIAL_COLORS);

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(0.9f);

        barChart.setData(data);
    }

    public static class DayAxisValueFormatter implements IAxisValueFormatter {
        private DecimalFormat mFormat;

        public DayAxisValueFormatter() {
            mFormat = new DecimalFormat("###,###,###,##0.0");
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(value) + " $";
        }
    }

    public static class MyAxisValueFormatter implements IAxisValueFormatter {

        public MyAxisValueFormatter() {
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return value + "i fwf ewf ";
        }
    }
}
