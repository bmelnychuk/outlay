package app.outlay.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.outlay.domain.model.Category;
import app.outlay.utils.IconUtils;
import app.outlay.utils.ResourceUtils;
import app.outlay.view.adapter.listener.OnCategoryClickListener;
import app.outlay.view.numpad.NumpadEditable;
import app.outlay.view.numpad.NumpadValidator;
import app.outlay.view.numpad.NumpadView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class CategoriesGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER = 0;
    private static final int CATEGORY = 1;

    protected List<Category> items;
    private OnCategoryClickListener clickListener;
    private NumpadEditable numpadEditable;
    private NumpadValidator numpadValidator;

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.clickListener = listener;
    }

    public void attachNumpadEditable(NumpadEditable numpadEditable, NumpadValidator validator) {
        this.numpadEditable = numpadEditable;
        this.numpadValidator = validator;
    }

    public CategoriesGridAdapter(List<Category> categories) {
        this.items = categories;
    }

    public CategoriesGridAdapter() {
        this(new ArrayList<>());
    }

    public void setItems(List<Category> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? HEADER : CATEGORY;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case HEADER:
                final View numpadView = inflater.inflate(app.outlay.R.layout.recycler_numpad, parent, false);
                GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) numpadView.getLayoutParams();
                params.height = parent.getMeasuredHeight() - (context.getResources().getDimensionPixelSize(app.outlay.R.dimen.category_item_height) * 2);

                return new NumpadViewHolder(numpadView);
            default:
                final View catView = inflater.inflate(app.outlay.R.layout.item_category, parent, false);
                return new CategoryViewHolder(catView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CategoryViewHolder) {
            CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
            Context context = categoryViewHolder.categoryContainer.getContext();

            Category currentOne = items.get(position - 1);
            int iconCodeRes = ResourceUtils.getIntegerResource(context, currentOne.getIcon());

            categoryViewHolder.categoryTitle.setText(currentOne.getTitle());
            Drawable categoryIcon = IconUtils.getCategoryIcon(context, iconCodeRes, currentOne.getColor(), app.outlay.R.dimen.category_icon);
            categoryViewHolder.categoryIcon.setImageDrawable(categoryIcon);
            categoryViewHolder.categoryIcon.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onCategoryClicked(currentOne);
                }
            });
        } else if (holder instanceof NumpadViewHolder) {
            NumpadViewHolder numpadViewHolder = (NumpadViewHolder) holder;
            numpadViewHolder.numpadView.attachEditable(numpadEditable, numpadValidator);
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        @Bind(app.outlay.R.id.categoryContainer)
        View categoryContainer;

        @Bind(app.outlay.R.id.categoryIcon)
        ImageView categoryIcon;

        @Bind(app.outlay.R.id.categoryTitle)
        TextView categoryTitle;

        public CategoryViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public class NumpadViewHolder extends RecyclerView.ViewHolder {
        @Bind(app.outlay.R.id.numpadView)
        NumpadView numpadView;

        public NumpadViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
