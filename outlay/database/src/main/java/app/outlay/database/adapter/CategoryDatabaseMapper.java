package app.outlay.database.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bmelnychuk on 10/25/16.
 */

public class CategoryDatabaseMapper {
    public app.outlay.domain.model.Category toCategory(Category daoCategory) {
        app.outlay.domain.model.Category category = new app.outlay.domain.model.Category();
        category.setColor(daoCategory.getColor());
        category.setIcon(daoCategory.getIcon());
        category.setOrder(daoCategory.getOrder());
        category.setId(daoCategory.getId().toString());
        category.setTitle(daoCategory.getTitle());

        return category;
    }

    public Category fromCategory(app.outlay.domain.model.Category category) {
        Category daoCategory = new Category();
        daoCategory.setColor(category.getColor());
        daoCategory.setIcon(category.getIcon());
        daoCategory.setOrder(category.getOrder());
        daoCategory.setId(category.getId() == null ? null : Long.valueOf(category.getId()));
        daoCategory.setTitle(category.getTitle());

        return daoCategory;
    }

    public List<app.outlay.domain.model.Category> toCategories(List<Category> daoCategoryList) {
        List<app.outlay.domain.model.Category> result = new ArrayList<>(daoCategoryList.size());
        for (Category c : daoCategoryList) {
            result.add(toCategory(c));
        }
        return result;
    }

    public List<Category> fromCategories(List<app.outlay.domain.model.Category> categoryList) {
        List<Category> result = new ArrayList<>(categoryList.size());
        for (app.outlay.domain.model.Category c : categoryList) {
            result.add(fromCategory(c));
        }
        return result;
    }
}
