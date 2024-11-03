package org.example.category.dto.function;

import org.example.category.dto.GetCategoriesResponse;
import org.example.category.entity.Category;

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
