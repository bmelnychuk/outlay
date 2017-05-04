package app.outlay.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.johnkil.print.PrintView;

import app.outlay.utils.IconUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class IconsGridAdapter extends RecyclerView.Adapter<IconsGridAdapter.CategoryViewHolder> {

    private List<String> items;
    private int activeColor;
    private String selectedIcon;


    public IconsGridAdapter(List<String> categories, int color, String activeIcon) {
        this.items = categories;
        this.activeColor = color;
        this.selectedIcon = activeIcon;
    }

    public IconsGridAdapter(List<String> categories, int color) {
        this(categories, color, null);
    }

    public IconsGridAdapter(List<String> categories) {
        this(categories, 0, null);
    }

    public void setActiveColor(int color) {
        this.activeColor = color;
        notifyDataSetChanged();
    }

    public void setActiveIcon(String icon) {
        this.selectedIcon = icon;
        notifyDataSetChanged();
    }

    public void setActiveItem(String icon, int color) {
        this.selectedIcon = icon;
        this.activeColor = color;
        notifyDataSetChanged();
    }


    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(app.outlay.R.layout.item_icon, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Context context = holder.categoryContainer.getContext();
        String currentOne = items.get(position);

        IconUtils.loadCategoryIcon(currentOne, holder.categoryIcon);
        if (currentOne.equals(selectedIcon)) {
            holder.categoryIcon.setIconColor(activeColor);
        } else {
            holder.categoryIcon.setIconColor(ContextCompat.getColor(context, app.outlay.R.color.icon_inactive));
        }

        holder.categoryIcon.setOnClickListener(v -> {
            selectedIcon = currentOne;
            notifyDataSetChanged();
        });
    }

    public String getSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        @Bind(app.outlay.R.id.categoryContainer)
        View categoryContainer;

        @Bind(app.outlay.R.id.categoryIcon)
        PrintView categoryIcon;

        public CategoryViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
