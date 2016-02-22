package com.outlay.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.outlay.R;
import com.outlay.model.Report;
import com.outlay.utils.FormatUtils;
import com.outlay.utils.IconUtils;
import com.outlay.view.progress.ProgressLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bogdan Melnychuk on 1/27/16.
 */
public class ReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_CHART = 0;
    private static final int TYPE_REPORT_ITEM = 1;


    private List<Report> reports;
    private double maxProgress;
    private ItemClickListener onItemClickListener;

    public ReportAdapter(List<Report> items) {
        this.reports = items;
        maxProgress = getMaxProgress();
    }

    public void setOnItemClickListener(ItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ReportAdapter() {
        this(new ArrayList<>());
    }

    public void setItems(List<Report> items) {
        this.reports = items;
        maxProgress = getMaxProgress();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_CHART : TYPE_REPORT_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder result;
        if (viewType == TYPE_CHART) {
            final View v = inflater.inflate(R.layout.layout_chart, parent, false);
            result = new ChartViewHolder(v);

        } else {
            final View v = inflater.inflate(R.layout.item_report, parent, false);
            result = new ReportViewHolder(v);
        }
        return result;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReportViewHolder) {
            ReportViewHolder reportHolder = (ReportViewHolder) holder;
            Report currentReport = reports.get(position - 1);
            reportHolder.amountText.setText(FormatUtils.formatAmount(currentReport.getAmount()));
            reportHolder.titleText.setText(currentReport.getTitle());
            IconUtils.loadCategoryIcon(currentReport.getIcon(), reportHolder.icon);
            reportHolder.progressLayout.setMaxProgress((int) (maxProgress * 10));
            reportHolder.progressLayout.setCurrentProgress((int) (currentReport.getAmount() * 10));
            reportHolder.icon.setIconColor(currentReport.getColor());
            reportHolder.reportContainer.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClicked(currentReport);
                }
            });
            //reportHolder.progressLayout.setLoadedColor(currentReport.getColor());
        } else if (holder instanceof ChartViewHolder) {
            ChartViewHolder charViewHolder = (ChartViewHolder) holder;
            updateChartData(reports, charViewHolder.chart);
            charViewHolder.chart.animateY(1000, Easing.EasingOption.EaseInOutQuad);
            charViewHolder.chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClicked(reports.get(h.getXIndex()));
                    }
                }

                @Override
                public void onNothingSelected() {
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return reports.size() + 1;
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.amount)
        TextView amountText;
        @Bind(R.id.title)
        TextView titleText;
        @Bind(R.id.progressLayout)
        ProgressLayout progressLayout;
        @Bind(R.id.icon)
        PrintView icon;
        @Bind(R.id.reportContainer)
        View reportContainer;

        public ReportViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public class ChartViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.chart)
        PieChart chart;

        public ChartViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            chart.getLegend().setEnabled(false);
            chart.setUsePercentValues(false);
            chart.setDescription("");
            chart.setDragDecelerationFrictionCoef(0.95f);
            chart.setDrawHoleEnabled(true);
            chart.setHoleColorTransparent(true);
            chart.setTransparentCircleColor(Color.WHITE);
            chart.setTransparentCircleAlpha(110);
            chart.setHoleRadius(35f);
            chart.setTransparentCircleRadius(38f);
            chart.setRotationEnabled(false);
            chart.setCenterTextColor(Color.WHITE);
            chart.setCenterTextSize(16);
        }
    }

    private void updateChartData(List<Report> reports, PieChart chart) {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        double sum = 0;
        for (int i = 0; i < reports.size(); i++) {
            Report r = reports.get(i);
            sum += r.getAmount();
            entries.add(new Entry((int) (r.getAmount() * 1000), i));
            labels.add(r.getTitle());
            colors.add(r.getColor());
        }

        PieDataSet dataSet = new PieDataSet(entries, "Outlay");
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(10f);
        dataSet.setColors(colors);

        PieData data = new PieData(labels, dataSet);
        data.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> FormatUtils.formatAmount((double)value / 1000));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);
        chart.setCenterText(FormatUtils.formatAmount(sum));
        chart.highlightValues(null);
        chart.invalidate();
    }

    private double getMaxProgress() {
        double max = -1;
        for (Report r : reports) {
            if (max < r.getAmount()) {
                max = r.getAmount();
            }
        }
        return max;
    }

    public interface ItemClickListener {
        void onItemClicked(Report report);
    }
}
