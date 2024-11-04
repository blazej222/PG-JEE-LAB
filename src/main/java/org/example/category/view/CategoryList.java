package org.example.category.view;


import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.component.ModelFunctionFactory;
import org.example.category.model.CategoriesModel;
import org.example.category.service.CategoryService;

@RequestScoped
@Named
public class CategoryList {
    private CategoryService categoryService;

    private CategoriesModel categories;

    private final ModelFunctionFactory functionFactory;

    @Inject
    public CategoryList(ModelFunctionFactory functionFactory) {
        this.functionFactory = functionFactory;
    }

    @EJB
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
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
