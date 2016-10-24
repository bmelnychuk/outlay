package com.outlay.impl;

import com.outlay.domain.model.Category;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class CategoryAdapter {
    public static Category toCategory(com.outlay.dao.Category daoCategory) {
        Category category = new Category();
        category.setColor(daoCategory.getColor());
        category.setIcon(daoCategory.getIcon());
        category.setOrder(daoCategory.getOrder());
        category.setId(daoCategory.getId());
        category.setTitle(daoCategory.getTitle());

        return category;
    }

    public static com.outlay.dao.Category fromCategory(Category category) {
        com.outlay.dao.Category daoCategory = new com.outlay.dao.Category();
        daoCategory.setColor(category.getColor());
        daoCategory.setIcon(category.getIcon());
        daoCategory.setOrder(category.getOrder());
        daoCategory.setId(category.getId());
        daoCategory.setTitle(category.getTitle());

        return daoCategory;
    }
}
