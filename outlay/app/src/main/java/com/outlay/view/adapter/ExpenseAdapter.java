package com.outlay.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.outlay.R;
import com.outlay.core.utils.DateUtils;
import com.outlay.domain.model.Expense;
import com.outlay.utils.FormatUtils;
import com.outlay.utils.IconUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> items;
    private OnExpenseClickListener onExpenseClickListener;

    public ExpenseAdapter(List<Expense> categories) {
        this.items = categories;
    }

    public ExpenseAdapter() {
        this(new ArrayList<>());
    }

    public void setItems(List<Expense> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setOnExpenseClickListener(OnExpenseClickListener onExpenseClickListener) {
        this.onExpenseClickListener = onExpenseClickListener;
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_expense, parent, false);
        final ExpenseViewHolder viewHolder = new ExpenseViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder holder, int position) {
        Expense expense = items.get(position);
        holder.note.setText(expense.getNote());
        holder.root.setOnClickListener(v -> {
            if (onExpenseClickListener != null) {
                onExpenseClickListener.onExpenseClicked(expense);
            }
        });
        holder.categoryAmount.setText(FormatUtils.formatAmount(expense.getAmount()));
        holder.categoryDate.setText(DateUtils.toShortString(expense.getReportedWhen()));
        holder.categoryTitle.setText(expense.getCategory().getTitle());
        IconUtils.loadCategoryIcon(expense.getCategory(), holder.categoryIcon);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.categoryNote)
        TextView note;

        @Bind(R.id.expenseContainer)
        View root;

        @Bind(R.id.categoryIcon)
        PrintView categoryIcon;

        @Bind(R.id.categoryTitle)
        TextView categoryTitle;

        @Bind(R.id.categoryDate)
        TextView categoryDate;

        @Bind(R.id.categoryAmount)
        TextView categoryAmount;

        public ExpenseViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public interface OnExpenseClickListener {
        void onExpenseClicked(Expense e);
    }
}
