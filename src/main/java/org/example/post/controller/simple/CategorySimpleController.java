package org.example.post.controller.simple;

import org.example.component.DtoFunctionFactory;
import org.example.post.controller.api.CategoryController;
import org.example.post.dto.GetCategoriesResponse;
import org.example.post.service.CategoryService;

public class CategorySimpleController implements CategoryController {
    private final CategoryService service;
    private final DtoFunctionFactory factory;

    public CategorySimpleController(CategoryService service, DtoFunctionFactory factory) {
        this.service = service;
        this.factory = factory;
    }

    @Override
    public GetCategoriesResponse getCategories() {
        return factory.categoriesToResponse().apply(service.findAll());
    }

}
