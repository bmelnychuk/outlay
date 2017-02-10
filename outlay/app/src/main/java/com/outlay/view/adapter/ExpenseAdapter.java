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
import com.outlay.core.utils.NumberUtils;
import com.outlay.utils.IconUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public abstract class ExpenseAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    protected List<Expense> items;
    protected OnExpenseClickListener onExpenseClickListener;

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
    public int getItemCount() {
        return items.size();
    }

    public interface OnExpenseClickListener {
        void onExpenseClicked(Expense e);
    }
}
