package com.outlay.database.adapter;

import com.outlay.domain.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bmelnychuk on 10/25/16.
 */

public class CategoryDatabaseMapper {
    public Category toCategory(com.outlay.database.dao.Category daoCategory) {
        Category category = new Category();
        category.setColor(daoCategory.getColor());
        category.setIcon(daoCategory.getIcon());
        category.setOrder(daoCategory.getOrder());
        category.setId(daoCategory.getId());
        category.setTitle(daoCategory.getTitle());

        return category;
    }

    public com.outlay.database.dao.Category fromCategory(Category category) {
        com.outlay.database.dao.Category daoCategory = new com.outlay.database.dao.Category();
        daoCategory.setColor(category.getColor());
        daoCategory.setIcon(category.getIcon());
        daoCategory.setOrder(category.getOrder());
        daoCategory.setId(category.getId());
        daoCategory.setTitle(category.getTitle());

        return daoCategory;
    }

    public List<Category> toCategories(List<com.outlay.database.dao.Category> daoCategoryList) {
        List<Category> result = new ArrayList<>(daoCategoryList.size());
        for (com.outlay.database.dao.Category c : daoCategoryList) {
            result.add(toCategory(c));
        }
        return result;
    }

    public List<com.outlay.database.dao.Category> fromCategories(List<Category> categoryList) {
        List<com.outlay.database.dao.Category> result = new ArrayList<>(categoryList.size());
        for (Category c : categoryList) {
            result.add(fromCategory(c));
        }
        return result;
    }
}
