package org.example.post.view;

import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.TransactionalException;
import lombok.Getter;
import lombok.Setter;
import org.example.component.ModelFunctionFactory;
import org.example.post.entity.Post;
import org.example.category.model.CategoryModel;
import org.example.post.model.PostEditModel;
import org.example.category.service.CategoryService;
import org.example.post.service.PostService;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ViewScoped
@Named
public class PostEdit implements Serializable {
    private PostService postService;

    private final ModelFunctionFactory factory;
    private CategoryService categoryService;

    @Setter
    @Getter
    private UUID id;

    @Getter
    private PostEditModel post;

    @Getter
    @Setter
    private List<CategoryModel> categories;

    @Inject
    public PostEdit(ModelFunctionFactory factory, FacesContext facesContext) {
        this.factory = factory;
    }

    public void init() throws IOException{
        Optional<Post> post = postService.findForCallerPrincipal(id);
        if(post.isPresent()){
            this.post = factory.postToEditModel().apply(post.get());
            this.categories = categoryService.findAll().stream().map(factory.categoryToModel()).toList();
        }
        else{
            FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");

        }

    }

    @EJB
    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    @EJB
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public String saveAction() throws IOException{

        try{
            postService.update(factory.updatePost().apply(postService.find(id).orElseThrow(),post));
            String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
            return viewId + "?faces-redirect=true&includeViewParams=true";
        }catch (TransactionalException ex)
        {
            if(ex.getCause() instanceof OptimisticLockException){
                init();
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Version collision"));
            }
        }
        return null;
    }
}
