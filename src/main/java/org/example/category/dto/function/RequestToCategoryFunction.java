package org.example.category.dto.function;

import org.example.category.dto.PutCategoryRequest;
import org.example.category.entity.Category;

import java.util.Collections;
import java.util.UUID;
import java.util.function.BiFunction;

public class RequestToCategoryFunction implements BiFunction<UUID, PutCategoryRequest, Category> {
    @Override
    public Category apply(UUID uuid, PutCategoryRequest request) {
        return Category.builder()
                .id(uuid)
                .name(request.getName())
                .posts(Collections.emptyList())
                .positionInHierarchy(request.getPositionInHierarchy())
                .build();
    }
}
