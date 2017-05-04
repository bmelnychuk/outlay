package app.outlay.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;

import app.outlay.core.utils.DateUtils;
import app.outlay.core.utils.NumberUtils;
import app.outlay.domain.model.Expense;
import app.outlay.utils.IconUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bmelnychuk on 2/10/17.
 */

public class GridExpensesAdapter extends ExpenseAdapter<GridExpensesAdapter.ExpenseGridItemViewHolder> {
    @Override
    public ExpenseGridItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(app.outlay.R.layout.recycler_grid_expense, parent, false);
        return new ExpenseGridItemViewHolder(v);
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
        @Bind(app.outlay.R.id.categoryNote)
        TextView note;

        @Bind(app.outlay.R.id.expenseContainer)
        View root;

        @Bind(app.outlay.R.id.categoryIcon)
        PrintView categoryIcon;

        @Bind(app.outlay.R.id.categoryTitle)
        TextView categoryTitle;

        @Bind(app.outlay.R.id.categoryDate)
        TextView categoryDate;

        @Bind(app.outlay.R.id.categoryAmount)
        TextView categoryAmount;

        public ExpenseGridItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
