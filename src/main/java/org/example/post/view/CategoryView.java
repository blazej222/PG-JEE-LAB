package org.example.post.view;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.example.component.ModelFunctionFactory;
import org.example.post.entity.Category;
import org.example.post.model.CategoryModel;
import org.example.post.service.CategoryService;
import org.example.post.service.PostService;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@Named
public class CategoryView {
    private final CategoryService categoryService;

    private final ModelFunctionFactory factory;
    private final PostService postService;

    @Getter
    @Setter
    private UUID id;

    @Getter
    private CategoryModel category;

    @Inject
    public CategoryView(final CategoryService categoryService, final ModelFunctionFactory factory, PostService postService) {
        this.categoryService = categoryService;
        this.factory = factory;
        this.postService = postService;
    }

    public void init() throws IOException {
        Optional<Category> category = categoryService.find(id);
        if(category.isPresent()){
            this.category = factory.categoryToModel().apply(category.get());
        } else{
            FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND,"Category not found");
        }
    }

    public String deletePost(UUID postId){
        System.out.println("Entered deletePost");
        postService.delete(postId);
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return viewId + "?faces-redirect=true&includeViewParams=true";
    }

}
