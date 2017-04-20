package app.outlay.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.outlay.core.utils.DateUtils;
import app.outlay.core.utils.NumberUtils;
import app.outlay.domain.model.Category;
import app.outlay.domain.model.Expense;
import app.outlay.utils.IconUtils;
import app.outlay.utils.ResourceUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bmelnychuk on 2/10/17.
 */

public class ListExpensesAdapter extends ExpenseAdapter<ListExpensesAdapter.ExpenseListItemViewHolder> {
    @Override
    public ExpenseListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(app.outlay.R.layout.recycler_list_expense, parent, false);
        return new ExpenseListItemViewHolder(v);
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
        Drawable categoryIcon = IconUtils.getCategoryIcon(context, iconCodeRes, currentOne.getColor(), app.outlay.R.dimen.report_category_icon);
        holder.icon.setImageDrawable(categoryIcon);

        holder.title.setText(currentOne.getTitle());
    }

    public class ExpenseListItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(app.outlay.R.id.icon)
        ImageView icon;

        @Bind(app.outlay.R.id.amount)
        TextView amount;

        @Bind(app.outlay.R.id.date)
        TextView date;

        @Bind(app.outlay.R.id.title)
        TextView title;

        @Bind(app.outlay.R.id.container)
        View container;

        public ExpenseListItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
