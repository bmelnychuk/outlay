package com.outlay.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.outlay.R;
import com.outlay.core.utils.DateUtils;
import com.outlay.core.utils.NumberUtils;
import com.outlay.domain.model.Expense;
import com.outlay.utils.IconUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bmelnychuk on 2/10/17.
 */

public class GridExpensesAdapter extends ExpenseAdapter<GridExpensesAdapter.ExpenseGridItemViewHolder> {
    @Override
    public ExpenseGridItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.recycler_grid_expense, parent, false);
        final ExpenseGridItemViewHolder viewHolder = new ExpenseGridItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExpenseGridItemViewHolder holder, int position) {
        Expense expense = items.get(position);
        holder.note.setText(expense.getNote());
        holder.root.setOnClickListener(v -> {
            if (onExpenseClickListener != null) {
                onExpenseClickListener.onExpenseClicked(expense);
            }
        });
        holder.categoryAmount.setText(NumberUtils.formatAmount(expense.getAmount()));
        holder.categoryDate.setText(DateUtils.toShortString(expense.getReportedWhen()));
        holder.categoryTitle.setText(expense.getCategory().getTitle());
        IconUtils.loadCategoryIcon(expense.getCategory(), holder.categoryIcon);
    }

    public class ExpenseGridItemViewHolder extends RecyclerView.ViewHolder {
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

        public ExpenseGridItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
