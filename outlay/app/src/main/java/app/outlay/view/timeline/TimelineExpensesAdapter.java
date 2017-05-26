package app.outlay.view.timeline;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.outlay.R;
import app.outlay.core.utils.DateUtils;
import app.outlay.core.utils.NumberUtils;
import app.outlay.domain.model.Category;
import app.outlay.domain.model.Expense;
import app.outlay.utils.IconUtils;
import app.outlay.utils.ResourceHelper;
import app.outlay.view.adapter.ExpenseAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderAdapter;

/**
 * Created by bmelnychuk on 2/10/17.
 */

public class TimelineExpensesAdapter extends RecyclerView.Adapter<TimelineExpensesAdapter.ExpenseViewHolder> implements StickyHeaderAdapter<TimelineExpensesAdapter.ExpenseHeaderViewHolder> {
    private List<Expense> expenses;
    private ExpenseAdapter.OnExpenseClickListener onExpenseClickListener;

    public TimelineExpensesAdapter() {
        expenses = new ArrayList<>();
    }

    public void setOnExpenseClickListener(ExpenseAdapter.OnExpenseClickListener onExpenseClickListener) {
        this.onExpenseClickListener = onExpenseClickListener;
    }

    public void setItems(List<Expense> items) {
        this.expenses = items;
        notifyDataSetChanged();
    }

    @Override
    public TimelineExpensesAdapter.ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.timeline_recycler_expense, parent, false);
        final ExpenseViewHolder viewHolder = new ExpenseViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TimelineExpensesAdapter.ExpenseViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        Context context = holder.container.getContext();
        holder.container.setOnClickListener(v -> {
            if (onExpenseClickListener != null) {
                onExpenseClickListener.onExpenseClicked(expense);
            }
        });

        holder.amount.setText(NumberUtils.formatAmount(expense.getAmount()));

        Category currentOne = expense.getCategory();
        int iconCodeRes = ResourceHelper.getIntegerResource(context, currentOne.getIcon());
        Drawable categoryIcon = IconUtils.getCategoryIcon(context, iconCodeRes, currentOne.getColor(), app.outlay.R.dimen.report_category_icon);
        holder.icon.setImageDrawable(categoryIcon);
        holder.note.setVisibility(TextUtils.isEmpty(expense.getNote()) ? View.GONE : View.VISIBLE);
        holder.note.setText(expense.getNote());
        holder.title.setText(currentOne.getTitle());
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    @Override
    public long getHeaderId(int position) {
        Expense expense = expenses.get(position);
        return DateUtils.toLongString(expense.getReportedWhen()).hashCode();
    }

    @Override
    public ExpenseHeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.timeline_recycler_header, parent, false);
        final ExpenseHeaderViewHolder viewHolder = new ExpenseHeaderViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindHeaderViewHolder(ExpenseHeaderViewHolder viewholder, int position) {
        Date date = expenses.get(position).getReportedWhen();
        viewholder.dateTextView.setText(DateUtils.toLongString(date));
    }

    public class ExpenseHeaderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.date)
        TextView dateTextView;

        public ExpenseHeaderViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        @Bind(app.outlay.R.id.icon)
        ImageView icon;

        @Bind(app.outlay.R.id.amount)
        TextView amount;

        @Bind(app.outlay.R.id.title)
        TextView title;

        @Bind(app.outlay.R.id.note)
        TextView note;

        @Bind(app.outlay.R.id.container)
        View container;

        public ExpenseViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
