package org.example.category.dto.function;

import org.example.category.dto.GetCategoryResponse;
import org.example.category.entity.Category;

import java.util.function.Function;

public class CategoryToResponseFunction implements Function<Category, GetCategoryResponse> {

    @Override
    public GetCategoryResponse apply(Category category) {
        return GetCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .positionInHierarchy(category.getPositionInHierarchy())
                .posts(category.getPosts().stream()
                        .map(post -> GetCategoryResponse.Post.builder()
                                .id(post.getId())
                                .content(post.getContent())
                                .build())
                        .toList())
                .build();
    }
}
