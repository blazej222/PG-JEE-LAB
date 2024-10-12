package org.example.post.dto.function;

import org.example.post.dto.GetCategoryResponse;
import org.example.post.entity.Category;

import java.util.function.Function;

public class CategoryToResponseFunction implements Function<Category, GetCategoryResponse> {

    @Override
    public GetCategoryResponse apply(Category category) {
        return GetCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .posts(category.getPosts().stream().toList())
                .build();
    }
}
