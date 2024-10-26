package org.example.post.view;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.example.component.ModelFunctionFactory;
import org.example.post.entity.Post;
import org.example.post.model.PostEditModel;
import org.example.post.service.PostService;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@ViewScoped
@Named
public class PostEdit implements Serializable {
    private final PostService postService;

    private final ModelFunctionFactory factory;

    @Setter
    @Getter
    private UUID id;

    @Getter
    private PostEditModel post;

    @Inject
    public PostEdit(PostService postService, ModelFunctionFactory factory) {
        this.postService = postService;
        this.factory = factory;
    }

    public void init() throws IOException{
        Optional<Post> post = postService.find(id);
        if(post.isPresent()){
            this.post = factory.postToEditModel().apply(post.get());
        }
        else{
            FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");

        }

    }

    public String saveAction(){
        postService.update(factory.updatePost().apply(postService.find(id).orElseThrow(),post));
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return viewId + "?faces-redirect=true&includeViewParams=true";
    }
}