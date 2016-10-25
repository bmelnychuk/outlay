package com.outlay.firebase.dto.adapter;

import com.outlay.domain.model.Category;
import com.outlay.firebase.dto.CategoryDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class CategoryAdapter {
    public Category toCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setColor(categoryDto.getColor());
        category.setIcon(categoryDto.getIcon());
        category.setOrder(categoryDto.getOrder());
        category.setId(categoryDto.getId());
        category.setTitle(categoryDto.getTitle());
        return category;
    }

    public CategoryDto fromCategory(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setColor(category.getColor());
        categoryDto.setIcon(category.getIcon());
        categoryDto.setOrder(category.getOrder());
        categoryDto.setId(category.getId());
        categoryDto.setTitle(category.getTitle());

        return categoryDto;
    }

    public List<Category> toCategories(List<CategoryDto> categoryDtos) {
        List<Category> result = new ArrayList<>(categoryDtos.size());
        for (CategoryDto c : categoryDtos) {
            result.add(toCategory(c));
        }
        return result;
    }

    public List<CategoryDto> fromCategories(List<Category> categoryList) {
        List<CategoryDto> result = new ArrayList<>(categoryList.size());
        for (Category c : categoryList) {
            result.add(fromCategory(c));
        }
        return result;
    }
}
