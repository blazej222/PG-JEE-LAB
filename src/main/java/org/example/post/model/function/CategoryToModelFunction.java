package org.example.post.model.function;

import org.example.post.entity.Category;
import org.example.post.model.CategoryModel;

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
