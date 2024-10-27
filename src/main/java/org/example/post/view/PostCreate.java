package org.example.post.view;

import jakarta.enterprise.context.Conversation;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.example.component.ModelFunctionFactory;
import org.example.post.model.CategoryModel;
import org.example.post.model.PostCreateModel;
import org.example.post.service.CategoryService;
import org.example.post.service.PostService;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ConversationScoped
@Named
@Log
@NoArgsConstructor(force = true)
public class PostCreate implements Serializable {
    private final PostService postService;

    private final CategoryService categoryService;

    private final ModelFunctionFactory factory;

    @Getter
    private PostCreateModel post;

    @Getter
    @Setter
    private List<CategoryModel> categories;

    private final Conversation conversation;


    @Inject
    public PostCreate(PostService postService, CategoryService categoryService, ModelFunctionFactory factory, Conversation conversation) {
        this.postService = postService;
        this.categoryService = categoryService;
        this.factory = factory;
        this.conversation = conversation;
    }

    public void init(){
        if(conversation.isTransient()){
            categories = categoryService.findAll().stream()
                    .map(factory.categoryToModel())
                    .collect(Collectors.toList());
            post = PostCreateModel.builder()
                    .id(UUID.randomUUID())
                    .build();
            conversation.begin();
        }
    }

    public String goToCategoryAction(){
        return "/post/post_create_category.xhtml?faces-redirect=true";
    }

    public Object goToBasicAction(){
        return "/post/post_create_basic.xhtml?faces-redirect=true";
    }

    public String cancelAction(){
        conversation.end();
        return "/post/post_list.xhtml?faces-redirect=true";
    }

    public String goToConfirmAction(){
        return "/post/post_create_confirm.xhtml?faces-redirect=true";
    }

    public String saveAction(){
        postService.create(factory.modelToPost().apply(post));
        conversation.end();
        return "/post/post_list.xhtml?faces-redirect=true";
    }

    public String getConversationId(){
        return conversation.getId();
    }

}
