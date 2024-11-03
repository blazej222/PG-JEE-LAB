package org.example.category.model.function;

import org.example.category.entity.Category;
import org.example.category.model.CategoryModel;

import java.io.Serializable;
import java.util.function.Function;

public class CategoryToModelFunction implements Function<Category, CategoryModel>, Serializable {

    public CategoryModel apply(Category category) {
        return CategoryModel.builder()
                .id(category.getId())
                .name(category.getName())
                .posts(category.getPosts())
                .positionInHierarchy(category.getPositionInHierarchy())
                .build();
    }
}
