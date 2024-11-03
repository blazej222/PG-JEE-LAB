package org.example.category.model.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import org.example.component.ModelFunctionFactory;
import org.example.category.entity.Category;
import org.example.category.model.CategoryModel;
import org.example.category.service.CategoryService;

import java.util.Optional;
import java.util.UUID;

@FacesConverter(forClass = CategoryModel.class, managed = true)
public class CategoryModelConverter implements Converter<CategoryModel> {
    private final CategoryService categoryService;

    private final ModelFunctionFactory factory;

    @Inject
    public CategoryModelConverter(CategoryService service, ModelFunctionFactory factory){
        this.categoryService = service;
        this.factory = factory;
    }

    @Override
    public CategoryModel getAsObject(FacesContext context, UIComponent component, String value) {
        if(value == null || value.isBlank()){
            return null;
        }
        Optional<Category> category = categoryService.find(UUID.fromString(value));
        return category.map(factory.categoryToModel()).orElse(null);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, CategoryModel value) {
        return value == null ? null : value.getId().toString();
    }
}
