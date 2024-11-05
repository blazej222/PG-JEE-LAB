package org.example.category.view;


import jakarta.ejb.EJB;
import jakarta.ejb.EJBAccessException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.component.ModelFunctionFactory;
import org.example.category.model.CategoriesModel;
import org.example.category.service.CategoryService;

import java.io.IOException;
import java.util.Collections;

@RequestScoped
@Named
public class CategoryList {
    private CategoryService categoryService;

    private CategoriesModel categories;

    private final ModelFunctionFactory functionFactory;

    @Inject
    public CategoryList(ModelFunctionFactory functionFactory) {
        this.functionFactory = functionFactory;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        System.out.println(externalContext.getRemoteUser());
    }

    public boolean verify()
    {
        return categoryService.verify();
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
