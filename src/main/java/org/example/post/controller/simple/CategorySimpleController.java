package org.example.post.controller.simple;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import org.example.component.DtoFunctionFactory;
import org.example.post.controller.api.CategoryController;
import org.example.post.dto.GetCategoriesResponse;
import org.example.post.service.CategoryService;

@ApplicationScoped
@NoArgsConstructor(force = true)
public class CategorySimpleController implements CategoryController {
    private final CategoryService service;
    private final DtoFunctionFactory factory;

    @Inject
    public CategorySimpleController(CategoryService service, DtoFunctionFactory factory) {
        this.service = service;
        this.factory = factory;
    }

    @Override
    public GetCategoriesResponse getCategories() {
        return factory.categoriesToResponse().apply(service.findAll());
    }

}
