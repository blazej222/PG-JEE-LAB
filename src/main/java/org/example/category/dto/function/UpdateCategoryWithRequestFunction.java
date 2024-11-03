package org.example.category.dto.function;

import org.example.category.dto.PatchCategoryRequest;
import org.example.category.entity.Category;

import java.util.function.BiFunction;

public class UpdateCategoryWithRequestFunction implements BiFunction<Category, PatchCategoryRequest, Category> {
    @Override
    public Category apply(Category category, PatchCategoryRequest request) {
        return Category.builder()
                .id(category.getId())
                .name(request.getName())
                .positionInHierarchy(request.getPositionInHierarchy())
                .posts(category.getPosts())
                .build();
    }
}
