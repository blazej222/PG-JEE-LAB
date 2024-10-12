package org.example.post.dto.function;

import org.example.post.dto.GetCategoriesResponse;
import org.example.post.entity.Category;

import java.util.List;
import java.util.function.Function;

public class CategoriesToResponseFunction implements Function<List<Category>, GetCategoriesResponse> {

    @Override
    public GetCategoriesResponse apply(List<Category> categories) {
        return GetCategoriesResponse.builder()
                .categories(categories.stream()
                        .map(category -> GetCategoriesResponse.Category.builder()
                                .id(category.getId())
                                .name(category.getName())
                                .build())
                        .toList())
                .build();
    }
}
