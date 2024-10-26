package org.example.post.view;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.component.ModelFunctionFactory;
import org.example.post.model.PostsModel;
import org.example.post.service.PostService;

@RequestScoped
@Named
public class PostList {
    private final PostService postService;

    private PostsModel posts;

    private final ModelFunctionFactory factory;

    @Inject
    public PostList(PostService postService, ModelFunctionFactory factory) {
        this.postService = postService;
        this.factory = factory;
    }

    public PostsModel getPosts() {
        if(posts == null) {
            posts = factory.postsToModel().apply(postService.findAll());
        }
        return posts;
    }

    public String deleteAction(PostsModel.Post post){
        postService.delete(post.getId());
        return "post_list?faces-redirect=true";
    }
}
