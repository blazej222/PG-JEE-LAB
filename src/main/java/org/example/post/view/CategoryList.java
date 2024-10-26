package org.example.post.view;


import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.component.ModelFunctionFactory;
import org.example.post.model.CategoriesModel;
import org.example.post.service.CategoryService;

@RequestScoped
@Named
public class CategoryList {
    private final CategoryService categoryService;

    private CategoriesModel categories;

    private final ModelFunctionFactory functionFactory;

    @Inject
    public CategoryList(CategoryService categoryService, ModelFunctionFactory functionFactory) {
        this.categoryService = categoryService;
        this.functionFactory = functionFactory;
    }

    public CategoriesModel getCategories(){
        if(categories == null){
            categories = functionFactory.categoriesToModel().apply(categoryService.findAll());
        }
        return categories;
    }

    public String deleteAction(CategoriesModel.Category category){
        categoryService.delete(category.getId());
        return "category_list?faces-redirect=true";
    }

}
