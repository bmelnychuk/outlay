package app.outlay.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import app.outlay.core.utils.NumberUtils;
import app.outlay.domain.model.Category;
import app.outlay.domain.model.Report;
import app.outlay.utils.IconUtils;
import app.outlay.view.model.CategorizedExpenses;
import app.outlay.view.progress.ProgressLayout;

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
            final View v = inflater.inflate(app.outlay.R.layout.layout_chart, parent, false);
            result = new ChartViewHolder(v);

        } else {
            final View v = inflater.inflate(app.outlay.R.layout.item_report, parent, false);
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
            Context context = charViewHolder.chart.getContext();

            updateChartData(charViewHolder.chart);

            charViewHolder.prev.setImageDrawable(IconUtils.getToolbarIcon(context, MaterialDesignIconic.Icon.gmi_arrow_left));
            charViewHolder.next.setImageDrawable(IconUtils.getToolbarIcon(context, MaterialDesignIconic.Icon.gmi_arrow_right));

            if (charViewHolder.chart.isDrawEntryLabelsEnabled()) {
                charViewHolder.hideLabels.setImageDrawable(IconUtils.getToolbarIcon(context, MaterialDesignIconic.Icon.gmi_format_size));
            } else {
                charViewHolder.hideLabels.setImageDrawable(IconUtils.getToolbarIcon(context, MaterialDesignIconic.Icon.gmi_format_clear));
            }

            charViewHolder.hideLabels.setOnClickListener(view -> {
                charViewHolder.chart.setDrawEntryLabels(!charViewHolder.chart.isDrawEntryLabelsEnabled());
                if (charViewHolder.chart.isDrawEntryLabelsEnabled()) {
                    charViewHolder.hideLabels.setImageDrawable(IconUtils.getToolbarIcon(context, MaterialDesignIconic.Icon.gmi_format_size));
                } else {
                    charViewHolder.hideLabels.setImageDrawable(IconUtils.getToolbarIcon(context, MaterialDesignIconic.Icon.gmi_format_clear));
                }

                charViewHolder.chart.invalidate();
            });

            //charViewHolder.chart.animateY(1000, Easing.EasingOption.EaseInOutQuad);
            charViewHolder.chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {

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
        @Bind(app.outlay.R.id.amount)
        TextView amountText;
        @Bind(app.outlay.R.id.title)
        TextView titleText;
        @Bind(app.outlay.R.id.progressLayout)
        ProgressLayout progressLayout;
        @Bind(app.outlay.R.id.icon)
        PrintView icon;
        @Bind(app.outlay.R.id.reportContainer)
        View reportContainer;

        public ReportViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public class ChartViewHolder extends RecyclerView.ViewHolder {
        @Bind(app.outlay.R.id.chart)
        PieChart chart;

        @Bind(app.outlay.R.id.hideLabels)
        ImageView hideLabels;

        @Bind(app.outlay.R.id.previous)
        ImageView prev;

        @Bind(app.outlay.R.id.next)
        ImageView next;

        public ChartViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            chart.getLegend().setEnabled(false);
            chart.setUsePercentValues(false);
            chart.getDescription().setEnabled(false);
            chart.setDragDecelerationFrictionCoef(0.95f);
            chart.setDrawHoleEnabled(true);
            chart.setHoleColor(Color.TRANSPARENT);
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
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        double sum = 0;
        for (int i = 0; i < categorizedExpenses.getCategories().size(); i++) {
            Category c = categorizedExpenses.getCategory(i);
            Report r = categorizedExpenses.getReport(c);
            sum += r.getTotalAmount().doubleValue();
            entries.add(new PieEntry((int) (r.getTotalAmount().doubleValue() * 1000), c.getTitle()));
            colors.add(c.getColor());
        }

        PieDataSet dataSet = new PieDataSet(entries, "Outlay");
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(10f);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
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
