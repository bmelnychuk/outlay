package app.outlay.view.autocomplete;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import app.outlay.domain.model.Category;
import app.outlay.utils.IconUtils;
import com.github.johnkil.print.PrintView;

import java.util.ArrayList;
import java.util.List;

public class CategoryAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private List<Category> items;
    private List<Category> suggestions;

    private LayoutInflater inflater;

    private Filter nameFilter = new NameFilter();

    private class NameFilter extends Filter {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Category) resultValue).getTitle();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint == null) {
                return filterResults;
            }

            suggestions.clear();

            String query = constraint.toString().toLowerCase();

            for (Category category : items) {
                String title = category.getTitle().toLowerCase();
                if (title.startsWith(query)) {
                    suggestions.add(category);
                }
            }

            filterResults.values = suggestions;
            filterResults.count = suggestions.size();

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results != null && results.count > 0) {
                notifyDataSetChanged();
            }
            else {
                notifyDataSetInvalidated();
            }
        }
    }

    public CategoryAutoCompleteAdapter() {
        super();
        this.items = new ArrayList<>();
        this.suggestions = new ArrayList<>();
    }

    public void setItems(List<Category> categoryList) {
        this.items = categoryList;
        this.suggestions = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public Category getItem(int position) {
        return suggestions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category current = suggestions.get(position);
        Context context = parent.getContext();
        ViewHolder vh;
        View view = convertView;
        if (view == null) {
            if (inflater == null) {
                inflater = LayoutInflater.from(context);
            }
            view = inflater.inflate(app.outlay.R.layout.item_autocomplete_category, null);
            vh = new ViewHolder();
            vh.categoryIcon = (PrintView) view.findViewById(app.outlay.R.id.categoryIcon);
            vh.categoryTitle = (TextView) view.findViewById(app.outlay.R.id.categoryTitle);

            view.setTag(vh);
        }
        else {
            vh = (ViewHolder) view.getTag();
        }
        vh.categoryTitle.setText(current.getTitle());
        IconUtils.loadCategoryIcon(current, vh.categoryIcon);
        return view;
    }

    class ViewHolder {
        TextView categoryTitle;
        PrintView categoryIcon;

    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }
}
