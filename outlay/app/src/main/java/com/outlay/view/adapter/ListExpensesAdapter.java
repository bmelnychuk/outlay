package com.outlay.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.outlay.R;
import com.outlay.core.utils.DateUtils;
import com.outlay.core.utils.NumberUtils;
import com.outlay.domain.model.Category;
import com.outlay.domain.model.Expense;
import com.outlay.utils.IconUtils;
import com.outlay.utils.ResourceUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bmelnychuk on 2/10/17.
 */

public class ListExpensesAdapter extends ExpenseAdapter<ListExpensesAdapter.ExpenseListItemViewHolder> {
    @Override
    public ExpenseListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.recycler_list_expense, parent, false);
        final ExpenseListItemViewHolder viewHolder = new ExpenseListItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExpenseListItemViewHolder holder, int position) {
        Expense expense = items.get(position);
        Context context = holder.container.getContext();
        holder.container.setOnClickListener(v -> {
            if (onExpenseClickListener != null) {
                onExpenseClickListener.onExpenseClicked(expense);
            }
        });

        holder.amount.setText(NumberUtils.formatAmount(expense.getAmount()));
        holder.date.setText(DateUtils.toShortString(expense.getReportedWhen()));

        Category currentOne = expense.getCategory();
        int iconCodeRes = ResourceUtils.getIntegerResource(context, currentOne.getIcon());
        Drawable categoryIcon = IconUtils.getCategoryIcon(context, iconCodeRes, currentOne.getColor(), R.dimen.report_category_icon);
        holder.icon.setImageDrawable(categoryIcon);

        holder.title.setText(currentOne.getTitle());
    }

    public class ExpenseListItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.icon)
        ImageView icon;

        @Bind(R.id.amount)
        TextView amount;

        @Bind(R.id.date)
        TextView date;

        @Bind(R.id.title)
        TextView title;

        @Bind(R.id.container)
        View container;

        public ExpenseListItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
