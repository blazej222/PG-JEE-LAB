package org.example.post.view;

import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.example.component.ModelFunctionFactory;
import org.example.category.model.CategoryModel;
import org.example.post.model.PostCreateModel;
import org.example.category.service.CategoryService;
import org.example.post.service.PostService;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ViewScoped
@Named
@Log
@NoArgsConstructor(force = true)
public class PostCreate implements Serializable {
    private PostService postService;

    private CategoryService categoryService;

    private final ModelFunctionFactory factory;

    @Getter
    @Setter
    private PostCreateModel post;

    @Getter
    @Setter
    private List<CategoryModel> categories;


    @Inject
    public PostCreate(ModelFunctionFactory factory) {
        this.factory = factory;
    }

    public void init(){
        categories = categoryService.findAll().stream()
                .map(factory.categoryToModel())
                .collect(Collectors.toList());
        post = PostCreateModel.builder()
                .id(UUID.randomUUID())
                .build();
    }

    @EJB
    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    @EJB
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public String saveAction(){
        if(post.getCategory() == null || post.getContent() == null){
            return null;
        }
        postService.create(factory.modelToPost().apply(post));
        return "/post/post_list.xhtml?faces-redirect=true";
    }

}
