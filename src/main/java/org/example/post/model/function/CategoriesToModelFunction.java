package org.example.post.model.function;

import org.example.post.entity.Category;
import org.example.post.model.CategoriesModel;

import java.util.List;
import java.util.function.Function;

public class CategoriesToModelFunction implements Function<List<Category>, CategoriesModel> {

    @Override
    public CategoriesModel apply(List<Category> categories) {
        return CategoriesModel.builder()
                .categories(categories.stream()
                        .map(category -> CategoriesModel.Category.builder()
                                .id(category.getId())
                                .name(category.getName())
                                .build())
                        .toList()
                )
                .build();


    }
}
