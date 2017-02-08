package com.outlay.view.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.outlay.R;
import com.outlay.core.utils.NumberUtils;
import com.outlay.domain.model.Category;
import com.outlay.domain.model.Report;
import com.outlay.utils.IconUtils;
import com.outlay.view.model.CategorizedExpenses;
import com.outlay.view.progress.ProgressLayout;

import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bogdan Melnychuk on 1/27/16.
 */
public class ReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_CHART = 0;
    private static final int TYPE_REPORT_ITEM = 1;

    private CategorizedExpenses categorizedExpenses;
    private double maxProgress;
    private ItemClickListener onItemClickListener;

    public ReportAdapter(CategorizedExpenses categorizedExpenses) {
        this.categorizedExpenses = categorizedExpenses;
        maxProgress = getMaxProgress();
    }

    public void setOnItemClickListener(ItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ReportAdapter() {
        this(new CategorizedExpenses());
    }

    public void setItems(CategorizedExpenses categorizedExpenses) {
        this.categorizedExpenses = categorizedExpenses;
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

            Report currentReport = categorizedExpenses.getReport(position - 1);
            Category currentCategory = categorizedExpenses.getCategory(position - 1);


            reportHolder.amountText.setText(NumberUtils.formatAmount(currentReport.getTotalAmount()));
            reportHolder.titleText.setText(currentCategory.getTitle());
            IconUtils.loadCategoryIcon(currentCategory.getIcon(), reportHolder.icon);
            reportHolder.progressLayout.setMaxProgress((int) (maxProgress * 10));
            reportHolder.progressLayout.setCurrentProgress(currentReport.getTotalAmount().multiply(new BigDecimal(10)).intValue());
            reportHolder.icon.setIconColor(currentCategory.getColor());
            reportHolder.reportContainer.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClicked(currentCategory, currentReport);
                }
            });
            //reportHolder.progressLayout.setLoadedColor(currentReport.getColor());
        } else if (holder instanceof ChartViewHolder) {
            ChartViewHolder charViewHolder = (ChartViewHolder) holder;
            updateChartData(charViewHolder.chart);
            //charViewHolder.chart.animateY(1000, Easing.EasingOption.EaseInOutQuad);
            charViewHolder.chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                    if (onItemClickListener != null) {
                        //onItemClickListener.onItemClicked(reports.get(h.getXIndex()));
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
        return categorizedExpenses == null ? 1 : categorizedExpenses.getCategoriesSize() + 1;
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

    private void updateChartData(PieChart chart) {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        double sum = 0;
        for (int i = 0; i < categorizedExpenses.getCategories().size(); i++) {
            Category c = categorizedExpenses.getCategory(i);
            Report r = categorizedExpenses.getReport(c);
            sum += r.getTotalAmount().doubleValue();
            entries.add(new Entry((int) (r.getTotalAmount().doubleValue() * 1000), i));
            labels.add(c.getTitle());
            colors.add(c.getColor());
        }

        PieDataSet dataSet = new PieDataSet(entries, "Outlay");
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(10f);
        dataSet.setColors(colors);

        PieData data = new PieData(labels, dataSet);
        data.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> NumberUtils.formatAmount((double) value / 1000));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);
        chart.setCenterText(NumberUtils.formatAmount(sum));
        chart.highlightValues(null);
        chart.invalidate();
    }

    private double getMaxProgress() {
        double max = -1;
        for (Category c : categorizedExpenses.getCategories()) {
            Report r = categorizedExpenses.getReport(c);
            if (max < r.getTotalAmount().doubleValue()) {
                max = r.getTotalAmount().doubleValue();
            }
        }
        return max;
    }

    public interface ItemClickListener {
        void onItemClicked(Category category, Report report);
    }
}
