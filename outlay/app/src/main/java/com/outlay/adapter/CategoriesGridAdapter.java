package com.outlay.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.outlay.R;
import com.outlay.adapter.listener.OnCategoryClickListener;
import com.outlay.domain.model.Category;
import com.outlay.utils.IconUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class CategoriesGridAdapter extends RecyclerView.Adapter<CategoriesGridAdapter.CategoryViewHolder> {

    public static class Style {
        public final int itemHeight;

        public Style(int itemHeight) {
            this.itemHeight = itemHeight;
        }
    }

    private Style style;
    protected List<Category> items;
    private OnCategoryClickListener clickListener;

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.clickListener = listener;
    }

    public CategoriesGridAdapter(List<Category> categories, Style style) {
        this.style = style;
        this.items = categories;
    }

    public CategoriesGridAdapter(Style style) {
        this(new ArrayList<>(), style);
    }

    public void setItems(List<Category> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_category, parent, false);
        final CategoryViewHolder viewHolder = new CategoryViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category currentOne = items.get(position);
        if (style != null) {
            holder.categoryContainer.getLayoutParams().height = style.itemHeight;
        }
        holder.categoryTitle.setText(currentOne.getTitle());
        IconUtils.loadCategoryIcon(currentOne, holder.categoryIcon);
        holder.categoryIcon.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onCategoryClicked(currentOne);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.categoryContainer)
        View categoryContainer;

        @Bind(R.id.categoryIcon)
        PrintView categoryIcon;

        @Bind(R.id.categoryTitle)
        TextView categoryTitle;

        public CategoryViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
